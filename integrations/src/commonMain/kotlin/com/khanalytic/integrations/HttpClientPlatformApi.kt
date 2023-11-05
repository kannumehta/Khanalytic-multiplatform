package com.khanalytic.integrations

import com.khanalytic.integrations.http.Cookie
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

abstract class HttpClientPlatformApi(private val httpClient: HttpClient) : PlatformApi {

}