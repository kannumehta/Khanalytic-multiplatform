package com.khanalytic.kmm.http

import io.ktor.client.request.*
import io.ktor.http.*

object HttpRequestBuilderExtensions {
    fun HttpRequestBuilder.cookies(cookies: List<Cookie>) {
        cookies.forEach { cookie ->
            cookie(
                name = cookie.key,
                value = cookie.value,
                domain = cookie.domain
            )
        }
    }

    fun HttpRequestBuilder.referer(value: String) { header("Referer", value) }

    fun HttpRequestBuilder.origin(value: String) { header("Origin", value) }
}