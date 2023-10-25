package com.khanalytic.kmm.importing.intgerations

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

abstract class HttpClientPlatformApi(private val httpClient: HttpClient) : PlatformApi {
    protected suspend fun get(url: String, headers: Map<String, String>, cookies: List<Cookie>): String {
        return httpClient.get(url) {
            headers.entries.forEach { entry -> header(entry.key, entry.value) }

            cookies.forEach { cookie ->
                cookie(
                    name = cookie.key,
                    value = cookie.value,
                    domain = cookie.domain
                )
            }
        }.bodyAsText()
    }

    protected suspend fun post(url: String, headers: Map<String, String>, cookies: List<Cookie>): String {
        return httpClient.post(url) {
            headers.entries.forEach { entry -> header(entry.key, entry.value) }

            cookies.forEach { cookie ->
                cookie(
                    name = cookie.key,
                    value = cookie.value,
                    domain = cookie.domain
                )
            }
        }.bodyAsText()
    }


}