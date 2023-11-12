package com.khanalytic.kmm.http.api

import com.khanalytic.kmm.http.requests.SignInRequest
import com.khanalytic.kmm.http.requests.SignUpRequest
import com.khanalytic.kmm.http.responses.UserResponse
import com.khanalytic.models.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserApi: KoinComponent {
    private val httpClient: HttpClient by inject()

    @Throws(Exception::class)
    suspend fun signUp(request: SignUpRequest): User = withContext(Dispatchers.Default) {
        httpClient.post(Utils.appUrl("user/sign_up")) {
            setBody(request)
        }.body<UserResponse>().user
    }

    @Throws(Exception::class)
    suspend fun signIn(request: SignInRequest): User = withContext(Dispatchers.Default)  {
        httpClient.post(Utils.appUrl("user/login")) {
            setBody(request)
        }.body<UserResponse>().user
    }
}