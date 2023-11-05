package com.khanalytic.kmm.http.api

import com.khanalytic.kmm.http.requests.SyncRequest
import com.khanalytic.kmm.http.requests.appendSyncRequest
import io.ktor.http.URLBuilder

object Utils {
    private const val BASE_URL = "https://a332-2601-641-500-af0-dd00-2593-15d7-85e1.ngrok-free.app"

    fun appUrl(path: String): String = "$BASE_URL/$path"

    fun syncUrl(path: String, request: SyncRequest): String {
        val urlBuilder = URLBuilder(appUrl(path))
        urlBuilder.parameters.appendSyncRequest(request)
        return urlBuilder.buildString()
    }
}