package com.khanalytic.integrations

import io.ktor.client.HttpClient

interface PlatformApiFactory {
    fun create(httpClient: HttpClient): PlatformApi
}