package com.khanalytic.kmm.importing.intgerations.models

import kotlinx.serialization.Serializable

@Serializable
data class Complaint(
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
    val remoteItemId: String
)