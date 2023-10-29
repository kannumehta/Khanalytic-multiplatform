package com.khanalytic.integrations.swiggy.responses

import kotlinx.serialization.Serializable

@Serializable
data class BrandDetailResponse(
    val data: BrandDetail
)

@Serializable
data class BrandDetail(
    val address: String,
    val name: String,
    val area: String,
    val locality: String
)