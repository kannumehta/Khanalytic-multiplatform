package com.khanalytic.models

import kotlinx.serialization.Serializable

@Serializable
data class Cookie(
    val name: String,
    val value: String,
    val domain: String? = null,
    val path: String? = null,
    val expires: Long? = null,
    val isSecure: Boolean? = null,
    val isHttpOnly: Boolean? = null,
    val maxAge: Long? = null
)
