package com.khanalytic.integrations.http

import io.ktor.client.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class HttpClientFactory {

    fun create(
        loggerImpl: Logger = Logger.SIMPLE,
        logLevel: LogLevel = LogLevel.ALL
    ): HttpClient =
        HttpClient {
            install(Logging) {
                logger = loggerImpl
                level = logLevel
            }

            install(ContentEncoding) {
                deflate(1.0F)
                gzip(0.9F)
            }
        }
}