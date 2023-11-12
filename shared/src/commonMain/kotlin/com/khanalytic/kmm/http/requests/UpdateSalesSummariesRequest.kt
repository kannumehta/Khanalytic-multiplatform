package com.khanalytic.kmm.http.requests

import com.khanalytic.models.Complaint
import com.khanalytic.models.SalesSummary
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
class UpdateSalesSummariesRequest(
    val summaries: List<SalesSummary>,
    val syncDates: List<LocalDate>,
    val platformBrandId: Long
)