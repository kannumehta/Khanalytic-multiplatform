package com.khanalytic.kmm.importing.intgerations.models

import kotlinx.serialization.Serializable

@Serializable
data class SalesSummary(
    val deliveredOrders: Int,
    val aov: Float,
    val sales: Float
)