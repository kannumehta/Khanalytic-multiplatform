package com.khanalytic.kmm.importing.intgerations.swiggy.responses

import com.khanalytic.kmm.importing.intgerations.models.SalesSummary
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SalesSummaryResponse(
    val data: Data
)

@Serializable
data class Data(
    val orderMetricsV4: OrderMetrics
)

@Serializable
data class OrderMetrics(
    @SerialName("orders_overview") val summary: Summary
)

@Serializable
data class Summary(
    val deliveredOrders: Int,
    val averageOrderValue: Float,
    val totalRevenue: Float,
)

fun Summary.toSalesSummary(): SalesSummary =
    SalesSummary(deliveredOrders = deliveredOrders, aov = averageOrderValue, sales = totalRevenue)