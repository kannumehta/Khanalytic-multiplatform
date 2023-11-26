package com.khanalytic.kmm.http.requests

import com.khanalytic.kmm.ui.screens.filter.AnalyticsFilter
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class AnalyticsReportRequest(
    val reportType: String,
    val endDate: LocalDate,
    val brandIds: List<Long>,
    val platformIds: List<Long>,
    val limit: Int = 5
)

fun AnalyticsFilter.toRequest(date: LocalDate) = AnalyticsReportRequest(
    reportType = reportType.toString().lowercase(),
    endDate = date,
    brandIds = brandIds.toList(),
    platformIds = platformIds.toList()
)
