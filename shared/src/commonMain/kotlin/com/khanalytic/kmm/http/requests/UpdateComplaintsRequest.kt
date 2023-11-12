package com.khanalytic.kmm.http.requests

import com.khanalytic.models.Complaint
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class UpdateComplaintsRequest(
    val complaints: List<Complaint>,
    val syncDates: List<LocalDate>,
    val platformBrandId: Long
)