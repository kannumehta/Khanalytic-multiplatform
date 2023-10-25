package com.khanalytic.kmm.importing.intgerations

import io.ktor.client.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.logging.*
import kotlinx.serialization.json.Json

class HttpClientFactory {

    private val serializer = Json { ignoreUnknownKeys = true }

    fun create(loggerImpl: Logger = Logger.SIMPLE, logLevel: LogLevel = LogLevel.ALL): HttpClient =
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