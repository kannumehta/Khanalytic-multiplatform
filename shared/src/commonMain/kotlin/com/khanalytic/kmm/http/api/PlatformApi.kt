package com.khanalytic.kmm.http.api

import com.khanalytic.kmm.http.requests.SyncRequest
import com.khanalytic.models.Platform
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PlatformApi: KoinComponent {
    private val httpClient: HttpClient by inject()

    suspend fun getPlatforms(request: SyncRequest): List<Platform> =
        httpClient.get(Utils.syncUrl("platform/list", request)).body()
}