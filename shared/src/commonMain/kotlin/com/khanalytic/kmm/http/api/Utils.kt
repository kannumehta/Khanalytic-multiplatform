package com.khanalytic.kmm.http.api

object Utils {
    private const val BASE_URL = "https://2f24-2601-641-500-af0-c490-25b2-fd6f-dff5.ngrok-free.app"

    fun appUrl(path: String): String = "$BASE_URL/$path"
}