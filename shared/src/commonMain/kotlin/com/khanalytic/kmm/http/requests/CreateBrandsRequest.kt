package com.khanalytic.kmm.http.requests

import com.khanalytic.models.Brand
import kotlinx.serialization.Serializable

@Serializable
data class CreateBrandsRequest(
    val platformId: Long,
    val brands: List<Brand>
)
