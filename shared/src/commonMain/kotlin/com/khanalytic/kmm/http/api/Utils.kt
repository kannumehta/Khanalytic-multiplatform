package com.khanalytic.kmm.http.api

object Utils {
    private const val BASE_URL = "https://a332-2601-641-500-af0-dd00-2593-15d7-85e1.ngrok-free.app"

    fun appUrl(path: String): String = "$BASE_URL/$path"
}