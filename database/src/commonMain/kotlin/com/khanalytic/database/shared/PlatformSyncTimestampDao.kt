package com.khanalytic.database.shared

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PlatformSyncTimestampDao : KoinComponent {
    private val database: Database by inject()

    suspend fun getPlatformSyncTimeStamp(platformId: Long): Instant? = withContext(Dispatchers.IO) {
        database.dbQuery.selectPlatformSyncTimestamp(platformId).executeAsOneOrNull()?.let {
            Instant.parse(it.lastSyncedAt)
        }
    }

    suspend fun setPlatformSyncTimeStamp(platformId: Long, lastSyncedAt: Instant) =
        withContext(Dispatchers.IO) {
            database.dbQuery.upsertPlatformSyncTimestamp(platformId, lastSyncedAt.toString())
        }
}