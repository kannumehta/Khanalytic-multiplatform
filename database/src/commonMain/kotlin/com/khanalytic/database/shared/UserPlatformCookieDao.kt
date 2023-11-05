package com.khanalytic.database.shared

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.khanalytic.models.UserPlatformCookie as ModelUserPlatformCookie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserPlatformCookieDao: KoinComponent {
    private val database: Database by inject()
    
    suspend fun insert(userPlatformCookie: ModelUserPlatformCookie): ModelUserPlatformCookie = withContext(Dispatchers.IO) {
        var lastInsertedId = 0L;
        database.dbQuery.transaction {
            database.dbQuery.insertUserPlatformCookie(
                userId = userPlatformCookie.userId,
                platformId = userPlatformCookie.platformId,
                cookiesJson = userPlatformCookie.toUserPlatformCookie().cookiesJson
            )
            lastInsertedId = database.dbQuery.selectLastUserPlatform().executeAsOne()
        }
        userPlatformCookie.copy(id = lastInsertedId)
    }

    suspend fun update(userPlatformCookie: ModelUserPlatformCookie) = withContext(Dispatchers.IO) {
        database.dbQuery.updateUserPlatformCookie(
            id = userPlatformCookie.id,
            userId = userPlatformCookie.userId,
            platformId = userPlatformCookie.platformId,
            cookiesJson = userPlatformCookie.toUserPlatformCookie().cookiesJson
        )
    }
    
    suspend fun upsert(userPlatformCookie: ModelUserPlatformCookie): ModelUserPlatformCookie {
        return if (userPlatformCookie.id > 0) {
            update(userPlatformCookie)
            userPlatformCookie
        } else {
            insert(userPlatformCookie)
        }
    }

    suspend fun getById(id: Long): ModelUserPlatformCookie = withContext(Dispatchers.IO) {
        database.dbQuery.selectUserPlatformCookieById(id).executeAsOne().toModelUserPlatformCookie()
    }

    suspend fun getByUserId(userId: Long): List<ModelUserPlatformCookie> =
        withContext(Dispatchers.IO) {
            database.dbQuery.selectUserPlatformCookiesByUser(userId).executeAsList()
                .map { it.toModelUserPlatformCookie() }
        }

    fun getFlowByUserId(userId: Long): Flow<List<ModelUserPlatformCookie>> =
        database.dbQuery.selectUserPlatformCookiesByUser(userId)
            .asFlow()
            .flowOn(Dispatchers.IO)
            .mapToList()
            .map { userPlatformCookies ->
                userPlatformCookies.map { it.toModelUserPlatformCookie() }
            }

    companion object {
        private val serializer = Json
        private fun UserPlatformCookie.toModelUserPlatformCookie(): ModelUserPlatformCookie =
            ModelUserPlatformCookie(
                id = id,
                userId = userId,
                platformId = platformId,
                cookies = serializer.decodeFromString(cookiesJson)
            )

        private fun ModelUserPlatformCookie.toUserPlatformCookie(): UserPlatformCookie =
            UserPlatformCookie(
                id = id,
                userId = userId,
                platformId = platformId,
                cookiesJson = serializer.encodeToString(cookies)
            )
    }
}