package com.khanalytic.integrations.http

import io.ktor.client.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.ConstantCookiesStorage
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent

class HttpClientFactory: KoinComponent {
    fun create(
        // TODo(kannumehta@): This logger should be removed.
        cookiesStorage: CookiesStorage,
        loggerImpl: Logger = Logger.SIMPLE,
        logLevel: LogLevel = LogLevel.ALL
    ): HttpClient =
        HttpClient {
            install(Logging) {
                logger = loggerImpl
                level = logLevel
            }

            install(HttpCookies) {
                storage = cookiesStorage
            }

            install(ContentEncoding) {
                deflate(1.0F)
                gzip(0.9F)
            }
        }
}