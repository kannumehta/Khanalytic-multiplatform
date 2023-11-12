package com.khanalytic.kmm.http.api

import com.khanalytic.kmm.http.requests.SyncRequest
import com.khanalytic.kmm.http.requests.UserApiRequest
import com.khanalytic.models.Platform
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PlatformApi: KoinComponent {
    private val httpClient: HttpClient by inject()

    @Throws(Exception::class)
    suspend fun getPlatforms(request: SyncRequest): List<Platform> =
        withContext(Dispatchers.Default) {
            httpClient.post(Utils.appUrl("platform/list")) {
                setBody(UserApiRequest(requestData = request))
            }.body()
        }
}