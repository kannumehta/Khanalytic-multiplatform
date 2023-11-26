package com.khanalytic.kmm.http.responses

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class AnalyticsReportResponse<T>(
    val reports: List<AnalyticsReport<T>>
)

@Serializable
data class AnalyticsReport<T>(
    val date: LocalDate,
    val report: T
)