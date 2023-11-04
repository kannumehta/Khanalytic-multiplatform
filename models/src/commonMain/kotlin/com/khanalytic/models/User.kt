package com.khanalytic.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long,
    val name: String,
    val email: String,
    val authToken: String,
    val isConfirmed: Boolean
)