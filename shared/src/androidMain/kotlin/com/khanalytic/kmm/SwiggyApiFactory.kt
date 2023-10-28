package com.khanalytic.kmm

import com.khanalytic.kmm.http.HttpClientFactory
import com.khanalytic.kmm.importing.intgerations.swiggy.SwiggyApi
import com.khanalytic.kmm.importing.intgerations.swiggy.SwiggyResponseParser
import com.khanalytic.kmm.importing.intgerations.Serialization

class SwiggyApiFactory {
    fun create(cookie: String): SwiggyApi {
        val serializer = Serialization.serializer
        val httpClient = HttpClientFactory().create()
        return SwiggyApi(serializer, httpClient, SwiggyResponseParser(serializer), cookie)
    }
}