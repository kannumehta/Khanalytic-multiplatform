package com.khanalytic.kmm.http.requests

import kotlinx.serialization.Serializable

@Serializable
data class UserApiRequest<T>(
    @Serializable val requestData: T,
    val user: UserAuthRequest? = null
)
