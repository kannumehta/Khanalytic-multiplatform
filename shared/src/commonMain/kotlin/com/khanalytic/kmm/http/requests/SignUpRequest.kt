package com.khanalytic.kmm.http.requests

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(val email: String, val name: String, val password: String)