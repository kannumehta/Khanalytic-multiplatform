package com.khanalytic.kmm.http

import io.ktor.client.HttpClient
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

class HttpClientFactory {

    @OptIn(ExperimentalSerializationApi::class)
    private val serializer = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        namingStrategy = JsonNamingStrategy.SnakeCase
    }

    fun create(
        // TODo(kannumehta@): This logger should be removed.
        loggerImpl: Logger = Logger.SIMPLE,
        logLevel: LogLevel = LogLevel.ALL
    ): HttpClient =
        HttpClient {

            install(ContentNegotiation) {
                json(serializer)
            }

            install(Logging) {
                logger = loggerImpl
                level = logLevel
            }

            install(ContentEncoding) {
                deflate(1.0F)
                gzip(0.9F)
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
}