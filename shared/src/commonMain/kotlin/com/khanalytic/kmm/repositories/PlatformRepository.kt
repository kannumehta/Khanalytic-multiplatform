package com.khanalytic.kmm.repositories

import com.khanalytic.database.shared.PlatformDao
import com.khanalytic.kmm.http.api.PlatformApi
import com.khanalytic.kmm.http.requests.SyncRequest
import com.khanalytic.models.Platform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Instant
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PlatformRepository: KoinComponent {
    private val platformApi: PlatformApi by inject()
    private val platformDao: PlatformDao by inject()

    private val mutex = Mutex()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var syncJob: Deferred<Unit>? = null

    suspend fun getAllPlatforms(): List<Platform> = platformDao.getAllPlatforms()
    fun getAllPlatformsAsFlow(): Flow<List<Platform>> = platformDao.getAllPlatformsAsFlow()

    suspend fun upsertAll(platforms: List<Platform>) = platformDao.upsertAll(platforms)

    suspend fun syncPlatforms() = mutex.withLock {
        if (syncJob == null) {
            syncJob = scope.async {
                syncPlatformsInBatches()
                syncJob = null
            }
        }
        syncJob?.await()
    }

    private suspend fun syncPlatformsInBatches() {
        while (true) {
            val lastSyncedPlatform = platformDao.getLastSyncedPlatform()
            val request = SyncRequest(
                lastUpdatedAt = lastSyncedPlatform?.updatedAt ?: Instant.fromEpochSeconds(0),
                idOffset = lastSyncedPlatform?.id ?: 0L
            )
            val platforms = platformApi.getPlatforms(request)
            upsertAll(platforms)
            if (platforms.size < request.limit) { break }
        }

    }
}