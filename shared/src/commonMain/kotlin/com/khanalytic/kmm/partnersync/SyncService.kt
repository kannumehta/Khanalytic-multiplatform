package com.khanalytic.kmm.partnersync

import com.khanalytic.database.shared.BrandDao
import com.khanalytic.database.shared.UserDao
import com.khanalytic.integrations.PlatformApi
import com.khanalytic.integrations.http.HttpClientFactory
import com.khanalytic.integrations.swiggy.SwiggyApiFactory
import com.khanalytic.kmm.http.HttpUserPlatformCookieStorageFactory
import com.khanalytic.models.Brand
import com.khanalytic.models.User
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncService: KoinComponent {
    private val brandsSyncService: BrandsSyncService by inject()
    private val menuSyncService: MenuSyncService by inject()
    private val userDao: UserDao by inject()
    private val brandDao: BrandDao by inject()

    private val httpUserPlatformCookieStorageFactory: HttpUserPlatformCookieStorageFactory by inject()
    private val httpClientFactory: HttpClientFactory by inject()
    private val swiggyApiFactory: SwiggyApiFactory by inject()

    @Throws(Exception::class)
    fun sync(userId: Long, platformId: Long, userPlatformCookieId : Long): Flow<SyncJobNode> =
        flow {
            // TODO(kannumehta@): Throw exceptions here so that they are logged to a backend.
            val user = userDao.getById(userId)
            if (user != null) {
                val platformApi = getPlatformApi(userId, platformId, userPlatformCookieId)
                val node = SyncJobNode.InternalNode("Syncing Data")
                startJob(node, user, platformApi, platformId, userPlatformCookieId) {
                    emit(node.clone())
                }
            } else {
                Napier.v("no logged in user, cannot sync data")
            }
        }

    private fun getPlatformApi(
        userId: Long,
        platformId: Long,
        userPlatformCookieId : Long
    ): PlatformApi {
        val httpClient = httpClientFactory.create(
            httpUserPlatformCookieStorageFactory.create(
                userId, platformId, userPlatformCookieId
            )
        )
        return swiggyApiFactory.create(httpClient)
    }

    private suspend fun startJob(
        node: SyncJobNode.InternalNode,
        user: User,
        platformApi: PlatformApi,
        platformId: Long,
        userPlatformCookieId: Long,
        onJobStateUpdated: suspend () -> Unit
    ) {
        syncBrands(node, user, platformApi, platformId, userPlatformCookieId, onJobStateUpdated)

        val brands = brandDao.selectAllBrandsByCookieId(userPlatformCookieId)
        val brandsAndSyncJobs = brands.map { Pair(it, SyncJobNode.InternalNode(it.name)) }
        node.children.addAll(brandsAndSyncJobs.map { it.second })
        brandsAndSyncJobs.forEach { brandAndSyncJob ->
            syncMenu(brandAndSyncJob.second, user, brandAndSyncJob.first, platformApi,
                onJobStateUpdated)
        }
    }

    private suspend fun syncBrands(
        node: SyncJobNode.InternalNode,
        user: User,
        platformApi: PlatformApi,
        platformId: Long,
        userPlatformCookieId: Long,
        onJobStateUpdated: suspend () -> Unit
    ) {
        startLeafJob("Brands", node, onJobStateUpdated) {
            brandsSyncService.syncBrands(user, platformApi, platformId, userPlatformCookieId)
        }
        onJobStateUpdated()
    }

    private suspend fun syncMenu(
        node: SyncJobNode.InternalNode,
        user: User,
        brand: Brand,
        platformApi: PlatformApi,
        onJobStateUpdated: suspend () -> Unit
    ) {
        startLeafJob("Menu", node, onJobStateUpdated) {
            brand.platformBrands.forEach { platformBrand ->
                menuSyncService.syncMenu(user, platformApi, platformBrand.id,
                    platformBrand.remoteBrandId)
            }
        }
        onJobStateUpdated()
    }

    private suspend fun startLeafJob(
        title: String,
        node: SyncJobNode.InternalNode,
        onJobStateUpdated: suspend () -> Unit,
        leafJob: suspend () -> Unit
    ) {
        val syncJob = SyncJobNode.LeafNode(title, SyncJobStatus.Processing)
        node.children.add(syncJob)
        onJobStateUpdated()
        leafJob()
        syncJob.status = SyncJobStatus.Processed
        onJobStateUpdated()
    }

}