package com.khanalytic.kmm.http.requests

import com.khanalytic.models.User
import kotlinx.serialization.Serializable

@Serializable
data class UserAuthRequest(
    val email: String,
    val authToken: String
)

fun User.toUserAuthRequest() = UserAuthRequest(email, authToken)
