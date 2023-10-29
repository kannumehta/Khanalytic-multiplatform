package com.khanalytic.kmm

import com.khanalytic.integrations.http.HttpClientFactory
import com.khanalytic.integrations.swiggy.SwiggyApi
import com.khanalytic.integrations.swiggy.SwiggyResponseParser
import com.khanalytic.integrations.Serialization

class SwiggyApiFactory {
    fun create(cookie: String): SwiggyApi {
        val serializer = Serialization.serializer
        val httpClient = HttpClientFactory().create()
        return SwiggyApi(serializer, httpClient, SwiggyResponseParser(serializer), cookie)
    }
}