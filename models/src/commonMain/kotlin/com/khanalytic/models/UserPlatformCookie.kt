package com.khanalytic.models

import kotlinx.serialization.Serializable

@Serializable
data class UserPlatformCookie(
    val id: Long,
    val userId: Long,
    val platformId: Long,
    val cookies: List<Cookie>,
)