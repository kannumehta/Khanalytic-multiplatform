package com.khanalytic.kmm.partnersync

import com.khanalytic.database.shared.BrandDao
import com.khanalytic.database.shared.UserDao
import com.khanalytic.integrations.PlatformApi
import com.khanalytic.integrations.http.HttpClientFactory
import com.khanalytic.integrations.swiggy.SwiggyApiFactory
import com.khanalytic.kmm.http.HttpUserPlatformCookieStorageFactory
import com.khanalytic.kmm.http.api.MissingDatesApi
import com.khanalytic.kmm.http.requests.MissingDatesRequest
import com.khanalytic.kmm.http.requests.UserApiRequest
import com.khanalytic.kmm.http.requests.toUserAuthRequest
import com.khanalytic.kmm.http.responses.MissingDates
import com.khanalytic.kmm.http.responses.PlatformBrandsMissingDates
import com.khanalytic.models.Brand
import com.khanalytic.models.Menu
import com.khanalytic.models.PlatformBrand
import com.khanalytic.models.User
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncService: KoinComponent {
    private val brandsSyncService: BrandsSyncService by inject()
    private val menuSyncService: MenuSyncService by inject()
    private val menuOrderSyncService: MenuOrderSyncService by inject()

    private val userDao: UserDao by inject()
    private val brandDao: BrandDao by inject()
    private val missingDatesApi: MissingDatesApi by inject()

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
        val brandsAndSyncJobs = brands
            .map { Pair(it, SyncJobNode.InternalNode(it.name)) }
            .filter { it.first.platformBrands.any { platformBrand -> platformBrand.active } }

        node.children.addAll(brandsAndSyncJobs.map { it.second })
        onJobStateUpdated()

        val platformBrandMissingDates = getMissingDates(user, brandsAndSyncJobs.map { it.first })
        brandsAndSyncJobs.forEach { brandAndSyncJob ->
            val platformBrandId = brandAndSyncJob.first.platformBrands.first().id
            val missingDates = platformBrandMissingDates.find {
                it.platformBrandId == platformBrandId
            }
            if (missingDates != null) {
                syncAllDataForBrand(brandAndSyncJob.second, user, brandAndSyncJob.first,
                    platformApi, missingDates.missingDates, onJobStateUpdated)
            }
            if (brandAndSyncJob.second.children.isEmpty()) {
                // Add a dummy node as processed that it can be shown on the UI as processed.
                brandAndSyncJob.second.children.add(
                    SyncJobNode.LeafNode("Dummy", SyncJobStatus.Processed)
                )
                onJobStateUpdated()
            }
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

    private suspend fun syncAllDataForBrand(
        node: SyncJobNode.InternalNode,
        user: User,
        brand: Brand,
        platformApi: PlatformApi,
        missingDates: MissingDates,
        onJobStateUpdated: suspend () -> Unit
    ) {
        val platformBrand = brand.platformBrands.first()
        if (missingDates.menuOrder.isNotEmpty()) {
            val menu = syncMenu(node, user, platformBrand, platformApi, onJobStateUpdated)
            startLeafJob("Orders", node, onJobStateUpdated) {
                menuOrderSyncService.syncOrders(user, menu, platformApi, platformBrand.id,
                    platformBrand.remoteBrandId, missingDates.menuOrder)
            }
        }
    }

    private suspend fun syncMenu(
        node: SyncJobNode.InternalNode,
        user: User,
        platformBrand: PlatformBrand,
        platformApi: PlatformApi,
        onJobStateUpdated: suspend () -> Unit
    ): Menu =
        startLeafJob("Menu", node, onJobStateUpdated) {
            menuSyncService.syncMenu(user, platformApi, platformBrand.id,
                platformBrand.remoteBrandId)
        }

    private suspend fun <T>startLeafJob(
        title: String,
        node: SyncJobNode.InternalNode,
        onJobStateUpdated: suspend () -> Unit,
        leafJob: suspend () -> T
    ): T {
        val syncJob = SyncJobNode.LeafNode(title, SyncJobStatus.Processing)
        node.children.add(syncJob)
        onJobStateUpdated()
        val result = leafJob()
        syncJob.status = SyncJobStatus.Processed
        onJobStateUpdated()
        return result
    }

    private suspend fun getMissingDates(
        user: User,
        brands: List<Brand>
    ): List<PlatformBrandsMissingDates> {
        val platformBrandIds =
            brands.flatMap { it.platformBrands.map { platformBrand -> platformBrand.id } }
        val request =
            UserApiRequest(MissingDatesRequest(platformBrandIds), user.toUserAuthRequest())
        return missingDatesApi.getMissingDates(request)
    }
}