package com.khanalytic.integrations.swiggy.responses

import com.khanalytic.integrations.responses.ComplaintIdsBatch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ComplaintsIdResponse(
    val data: ComplaintsData
)

@Serializable
data class ComplaintsData(
    @SerialName("getComplaints") val container: ComplaintsContainer
)

@Serializable
data class ComplaintsContainer(
    val complaints: List<ComplaintId>,
    val nextToken: String? = null
)

@Serializable
data class ComplaintId(
    @SerialName("complaintId") val id: String
)

fun ComplaintsIdResponse.toComplaintIdsBatch(): ComplaintIdsBatch =
    ComplaintIdsBatch(data.container.complaints.map { it.id }, data.container.nextToken)