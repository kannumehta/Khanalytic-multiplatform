package com.khanalytic.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Platform(
    val id: Long,
    val name: String,
    @Serializable(with = InstantSerializer::class) val createdAt: Instant,
    @Serializable(with = InstantSerializer::class) val updatedAt: Instant,
    val loginUrl: String? = null,
) {
    fun isZomato(): Boolean = name.lowercase() == "zomato"
    fun isSwiggy(): Boolean = name.lowercase() == "swiggy"
}
