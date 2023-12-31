package com.khanalytic.kmm.http.requests

import com.khanalytic.models.MenuOrder
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class UpdateMenuOrdersRequest(
    val orders: List<MenuOrder>,
    val syncDate: LocalDate,
    val platformBrandId: Long,
)