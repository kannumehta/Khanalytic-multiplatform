package com.khanalytic.kmm.http.responses

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class PlatformBrandsMissingDates(
    val platformBrandId: Long,
    val missingDates: MissingDates
)

@Serializable
data class MissingDates(
    val menuOrder: List<LocalDate>,
    val complaint: List<LocalDate>,
)