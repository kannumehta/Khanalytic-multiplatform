package com.khanalytic.kmm.importing.intgerations

import kotlinx.serialization.json.Json

object Serialization {
    val serializer = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
}