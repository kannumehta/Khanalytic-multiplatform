package com.khanalytic.kmm

import com.khanalytic.kmm.http.HttpClientFactory
import com.khanalytic.kmm.importing.intgerations.swiggy.SwiggyApi
import com.khanalytic.kmm.importing.intgerations.swiggy.SwiggyResponseParser
import com.khanalytic.kmm.importing.intgerations.swiggy.SwiggySerialization
import kotlinx.serialization.json.Json

class SwiggyApiFactory {
    fun create(cookie: String): SwiggyApi {
        val httpClient = HttpClientFactory().create()
        return SwiggyApi(httpClient, SwiggyResponseParser(SwiggySerialization.serializer), cookie)
    }
}