package com.khanalytic.integrations.models

import kotlinx.serialization.Serializable

@Serializable
data class SalesSummary(
    val deliveredOrders: Int,
    val aov: Float,
    val sales: Float
)