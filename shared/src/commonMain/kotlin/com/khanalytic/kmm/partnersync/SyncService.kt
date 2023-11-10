package com.khanalytic.kmm.partnersync

import com.khanalytic.database.shared.UserDao
import com.khanalytic.integrations.PlatformApi
import com.khanalytic.integrations.http.HttpClientFactory
import com.khanalytic.integrations.swiggy.SwiggyApiFactory
import com.khanalytic.kmm.http.HttpUserPlatformCookieStorageFactory
import com.khanalytic.models.User
import com.khanalytic.models.UserPlatformCookie
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncService: KoinComponent {
    private val brandsSyncService: BrandsSyncService by inject()
    private val userDao: UserDao by inject()

    private val httpUserPlatformCookieStorageFactory: HttpUserPlatformCookieStorageFactory by inject()
    private val httpClientFactory: HttpClientFactory by inject()
    private val swiggyApiFactory: SwiggyApiFactory by inject()

    @Throws(Exception::class)
    fun sync(userId: Long, platformId: Long, userPlatformCookieId : Long): Flow<List<SyncJob>> =
        flow {
            // TODO(kannumehta@): Throw exceptions here so that they are logged to a backend.
            val user = userDao.getById(userId)
            if (user != null) {
                val platformApi = getPlatformApi(userId, platformId, userPlatformCookieId)
                startJob(this, user, platformApi, platformId, userPlatformCookieId)
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
        flowCollector: FlowCollector<List<SyncJob>>,
        user: User,
        platformApi: PlatformApi,
        platformId: Long,
        userPlatformCookieId: Long
    ) {
        val allSyncJobs = mutableListOf<SyncJob>()
        val brandsSyncJob = SyncJob("Syncing Brands")
        allSyncJobs.add(brandsSyncJob)
        flowCollector.emit(allSyncJobs)
        syncBrands(user, platformApi, platformId, userPlatformCookieId, brandsSyncJob)
        flowCollector.emit(allSyncJobs)
    }

    private suspend fun syncBrands(
        user: User,
        platformApi: PlatformApi,
        platformId: Long,
        userPlatformCookieId: Long,
        job: SyncJob
    ) {
        job.status = SyncJobStatus.Processing
        brandsSyncService.syncBrands(user, platformApi, platformId, userPlatformCookieId)
        job.status = SyncJobStatus.Processed
        job.title = "Synced Brands"
    }

}