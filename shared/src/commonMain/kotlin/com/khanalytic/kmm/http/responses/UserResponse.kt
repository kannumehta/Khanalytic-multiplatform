package com.khanalytic.kmm.http.responses

import com.khanalytic.models.User
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val user: User
)