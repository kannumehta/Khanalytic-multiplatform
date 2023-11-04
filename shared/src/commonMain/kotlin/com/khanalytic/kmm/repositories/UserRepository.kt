package com.khanalytic.kmm.repositories

import com.khanalytic.database.shared.UserDao
import com.khanalytic.kmm.http.api.UserApi
import com.khanalytic.kmm.http.requests.SignInRequest
import com.khanalytic.kmm.http.requests.SignUpRequest
import com.khanalytic.models.User
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserRepository: KoinComponent {
    private val userApi: UserApi by inject()
    private val userDao: UserDao by inject()
    suspend fun signUp(request: SignUpRequest) = userDao.insert(userApi.signUp(request))

    suspend fun signIn(request: SignInRequest) = userDao.insert(userApi.signIn(request))

    fun getLoggedInUser(): Flow<User?> = userDao.selectFirstUser()
}