package com.khanalytic.kmm.importing.intgerations.swiggy

import kotlinx.serialization.json.Json

object SwiggySerialization {
    val serializer = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
}