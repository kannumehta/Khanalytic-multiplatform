package com.khanalytic.integrations

import kotlinx.serialization.json.Json

object Serialization {
    val serializer: Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
}