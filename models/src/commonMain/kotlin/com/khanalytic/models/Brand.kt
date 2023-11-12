package com.khanalytic.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Brand(
    val id: Long,
    val name: String,
    val address: String,
    val latitude: Double?,
    val longitude: Double?,
    @Serializable(with = InstantSerializer::class) val createdAt: Instant,
    @Serializable(with = InstantSerializer::class) val updatedAt: Instant,
    val platformBrands: List<PlatformBrand>
) {
    fun latitude(): Double = latitude ?: 0.0
    fun longitude(): Double = longitude ?: 0.0
}
