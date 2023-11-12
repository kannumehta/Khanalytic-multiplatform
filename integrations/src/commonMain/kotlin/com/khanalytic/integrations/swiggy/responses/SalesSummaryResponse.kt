package com.khanalytic.integrations.swiggy.responses

import com.khanalytic.models.SalesSummary
import kotlinx.datetime.LocalDate
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

fun Summary.toSalesSummary(platformBrandId: Long, date: LocalDate): SalesSummary = SalesSummary(
    platformBrandId = platformBrandId,
    deliveredOrders = deliveredOrders,
    date = date,
    aov = averageOrderValue,
    sales = totalRevenue
)