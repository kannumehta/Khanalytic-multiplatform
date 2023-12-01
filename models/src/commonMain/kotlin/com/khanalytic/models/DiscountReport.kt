package com.khanalytic.models

import kotlinx.serialization.Serializable

@Serializable
data class DiscountReport(
    val description: String,
    val brandId: Int,
    val brandName: String,
    val platformId: Int,
    val platformName: String,
    val totalOrders: Int,
    val subtotal: Float,
    val restaurantPromoDiscount: Float
)