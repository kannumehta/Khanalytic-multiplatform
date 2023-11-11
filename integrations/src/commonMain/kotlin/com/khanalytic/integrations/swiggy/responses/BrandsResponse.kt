package com.khanalytic.integrations.swiggy.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BrandsResponse(
    val outlets: List<Outlet>
)

@Serializable
data class Outlet(
    @SerialName("rest_rid") val resId: Long,
    val enabled: Boolean
)
