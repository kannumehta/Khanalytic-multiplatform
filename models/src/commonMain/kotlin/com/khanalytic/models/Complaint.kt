package com.khanalytic.models

import kotlinx.serialization.Serializable

@Serializable
data class Complaint(
    val platformBrandId: Long,
    val remoteComplaintId: String,
    val remoteOrderId: String,
    val issueType: String,
    val complaintState: String,
    val complaintStatus: String,
    val resolutionStatus: String,
    val items: List<ComplaintItem>,
    val customerComments: List<String>,
    val amountRefunded: Float? = null
)

@Serializable
data class ComplaintItem(
    val platformBrandId: Long,
    val remoteOrderId: String,
    val remoteComplaintId: String,
    val remoteItemId: String
)