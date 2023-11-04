package com.khanalytic.database.shared

import com.khanalytic.models.User as ModelUser
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserDao : KoinComponent {
    private val database: Database by inject()
    suspend fun insert(user: ModelUser): Unit = withContext(Dispatchers.IO) {
        database.dbQuery.insertUser(
            id = user.id,
            email = user.email,
            name = user.name,
            authToken = user.authToken,
            isVerified = user.isVerified
        )
    }

    fun selectFirstUser(): Flow<ModelUser?> =
        database.dbQuery.selectFirstUser().asFlow()
            .flowOn(Dispatchers.IO)
            .mapToList()
            .map { it.firstOrNull()?.toModelUser() }

    suspend fun deleteAllUsers(): Unit = withContext(Dispatchers.IO) {
        database.dbQuery.deleteAllUsers()
    }

    companion object {
        private fun User.toModelUser(): ModelUser = ModelUser(
            id = id,
            email = email,
            name = name,
            authToken = authToken,
            isVerified = isVerified == true
        )
    }
}