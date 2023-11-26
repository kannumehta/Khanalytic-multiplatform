package com.khanalytic.kmm.http.requests

import kotlinx.serialization.Serializable

@Serializable
data class GetMenuRequest(
    val brandId: Long,
    val platformIds: List<Long>
)