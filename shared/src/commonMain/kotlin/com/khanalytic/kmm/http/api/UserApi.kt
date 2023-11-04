package com.khanalytic.kmm.http.api

import com.khanalytic.kmm.http.requests.SignInRequest
import com.khanalytic.kmm.http.requests.SignUpRequest
import com.khanalytic.kmm.http.responses.UserResponse
import com.khanalytic.models.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserApi: KoinComponent {
    private val httpClient: HttpClient by inject()

    suspend fun signUp(request: SignUpRequest): User {
        return httpClient.post(Utils.appUrl("user/sign_up")) {
            setBody(request)
        }.body<UserResponse>().user
    }

    suspend fun signIn(request: SignInRequest): User {
        return httpClient.post(Utils.appUrl("user/login")) {
            setBody(request)
        }.body<UserResponse>().user
    }
}