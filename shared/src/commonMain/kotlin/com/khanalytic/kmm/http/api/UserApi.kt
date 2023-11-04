package com.khanalytic.kmm.http.api

import com.khanalytic.kmm.http.requests.SignInRequest
import com.khanalytic.kmm.http.requests.SignUpRequest
import com.khanalytic.models.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class UserApi(private val httpClient: HttpClient) {
    suspend fun signUp(request: SignUpRequest): User {
        return httpClient.post(Utils.appUrl("user/sign_up")) {
            setBody(request)
        }.body()
    }

    suspend fun signIn(request: SignInRequest): User {
        return httpClient.post(Utils.appUrl("user/sign_in")) {
            setBody(request)
        }.body()
    }
}