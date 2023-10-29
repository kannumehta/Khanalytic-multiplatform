package com.khanalytic.integrations

import com.khanalytic.integrations.http.Cookie
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

abstract class HttpClientPlatformApi(private val httpClient: HttpClient) : PlatformApi {
    protected suspend fun get(
        url: String,
        requestBuilderBlock: (HttpRequestBuilder) -> Unit = {}
    ): String {
        return httpClient.get(url) { requestBuilderBlock(this) }.bodyAsText()
    }

    protected suspend fun post(
        url: String,
        headers: Map<String, String>,
        cookies: List<Cookie>,
        requestBuilderBlock: (HttpRequestBuilder) -> Unit = {}
    ): String {
        return httpClient.post(url) {
            headers.entries.forEach { entry -> header(entry.key, entry.value) }

            cookies.forEach { cookie ->
                cookie(
                    name = cookie.key,
                    value = cookie.value,
                    domain = cookie.domain
                )
            }

            requestBuilderBlock(this)
        }.bodyAsText()
    }
}