package com.khanalytic.models

import kotlinx.serialization.Serializable

@Serializable
data class MenuItemStats(
    val stats: List<MenuItemStat>
)

@Serializable
data class MenuItemStat(
    val platformBrandId: Long,
    val remoteItemId: String,
    val revenue: Float,
    val deliveredOrders: Int,
    val complaints: Int
)