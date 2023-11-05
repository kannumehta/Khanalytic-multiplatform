package com.khanalytic.database.shared

import com.khanalytic.models.InstantUtils.toLocalDateTimeUtc
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import com.khanalytic.models.Platform as ModelPlatform
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PlatformDao: KoinComponent {
    private val database: Database by inject()

    suspend fun getAllPlatforms(): List<ModelPlatform> = withContext(Dispatchers.IO) {
        database.dbQuery.selectAllPlatforms().executeAsList().map { it.toModelPlatform() }
    }

    fun getAllPlatformsAsFlow(): Flow<List<ModelPlatform>> =
        database.dbQuery.selectAllPlatforms().asFlow().flowOn(Dispatchers.IO).mapToList()
            .map { platforms -> platforms.map { it.toModelPlatform() } }

    suspend fun upsertAll(platforms: List<ModelPlatform>) = withContext(Dispatchers.IO){
        database.dbQuery.transaction {
            platforms.forEach { upsert(it) }
        }
    }

    suspend fun getLastSyncedPlatform(): ModelPlatform? = withContext(Dispatchers.IO) {
        database.dbQuery.lastSycnedPlatform().executeAsOneOrNull()?.toModelPlatform()
    }

    private fun upsert(platform: ModelPlatform) = database.dbQuery.upsertPlatform(
        id = platform.id,
        name = platform.name,
        createdAt = platform.createdAt.toString(),
        updatedAt = platform.updatedAt.toString()
    )

    companion object {
        private fun Platform.toModelPlatform(): ModelPlatform =
            ModelPlatform(
                id = id,
                name = name,
                createdAt = Instant.parse(createdAt),
                updatedAt = Instant.parse(updatedAt)
            )
    }
}