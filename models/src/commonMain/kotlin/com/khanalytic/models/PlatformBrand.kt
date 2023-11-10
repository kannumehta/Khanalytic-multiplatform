package com.khanalytic.models

import kotlinx.serialization.Serializable

@Serializable
data class PlatformBrand(
    val id: Long,
    val platformId: Long,
    val remoteBrandId: String,
    val brandId: Long,
    val userPlatformCookieId: Long? = null
)
