package com.khanalytic.integrations.swiggy.responses

import com.khanalytic.models.Brand
import com.khanalytic.models.Location
import com.khanalytic.models.PlatformBrand
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class BrandDetailResponse(
    val data: BrandDetail
)

@Serializable
data class BrandDetail(
    val address: String,
    val name: String,
    val area: String,
    val locality: String
)

fun BrandDetail.toBrand(
    platformId: Long,
    remoteBrandId: String,
    location: Location,
    active: Boolean
): Brand =
    // id, createdAt, updatedAt don't matter when creating or updating the brands on the server.
    Brand(
        id = 0,
        name = name,
        address = address,
        latitude = location.latitude,
        longitude = location.longitude,
        createdAt = Instant.fromEpochSeconds(0),
        updatedAt = Instant.fromEpochSeconds(0),
        platformBrands = listOf(
            // Note that id, platformId and remoteBrandId will be ignored in the request. They only
            // matter in response.
            PlatformBrand(id = 0, platformId = platformId, remoteBrandId = remoteBrandId,
                brandId = 0, active = active)
        )
    )