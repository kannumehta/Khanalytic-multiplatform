package com.khanalytic.kmm

import com.khanalytic.kmm.http.HttpClientFactory
import com.khanalytic.kmm.importing.intgerations.swiggy.SwiggyApi
import com.khanalytic.kmm.importing.intgerations.swiggy.SwiggyResponseParser
import kotlinx.serialization.json.Json

class SwiggyApiFactory {
    private val serializer = Json { ignoreUnknownKeys = true }

    fun create(cookie: String): SwiggyApi {
        val httpClient = HttpClientFactory().create()
        return SwiggyApi(httpClient, SwiggyResponseParser(serializer), cookie)
    }
}