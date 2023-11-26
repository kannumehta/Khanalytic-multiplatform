package com.khanalytic.kmm.http.api

import com.khanalytic.kmm.http.requests.SyncRequest
import com.khanalytic.kmm.http.requests.appendSyncRequest
import io.ktor.http.URLBuilder

object Utils {
    private const val BASE_URL = "https://f33f-2601-641-500-af0-e0a9-fe6e-e9e0-5adc.ngrok-free.app"

    fun appUrl(path: String): String = "$BASE_URL/$path"
}