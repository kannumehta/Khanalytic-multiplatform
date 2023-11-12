package com.khanalytic.models

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class SalesSummary(
    val platformBrandId: Long,
    val deliveredOrders: Int,
    val date: LocalDate,
    val aov: Float,
    val sales: Float
)