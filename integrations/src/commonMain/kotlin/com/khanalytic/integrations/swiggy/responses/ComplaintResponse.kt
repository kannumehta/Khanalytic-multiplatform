package com.khanalytic.integrations.swiggy.responses

import com.khanalytic.models.Complaint
import com.khanalytic.models.ComplaintItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ComplaintResponse(
    val data: ComplaintResponseData
)

@Serializable
data class ComplaintResponseData(
    @SerialName("getComplaintOrderDetails") val complaint: ComplaintData
)

@Serializable
data class ComplaintData(
    val complaintId: String,
    val orderId: String,
    val issueType: String,
    val complaintState: String,
    val complaintStatus: String,
    val customerComments: List<String>,
    @SerialName("rootCauses") val items: List<ComplaintDataItem>,
    @SerialName("restaurantResolution") val resolution: ComplaintResolution
)

@Serializable
data class ComplaintDataItem(
    val itemId: String
)

@Serializable
data class ComplaintResolution(
    val amountRefunded: Float? = null,
    val resolutionStatus: String,
)

fun ComplaintResponse.toComplaint(platformBrandId: Long): Complaint =
    data.complaint.toComplaint(platformBrandId)
fun ComplaintData.toComplaint(platformBrandId: Long): Complaint = Complaint(
    platformBrandId = platformBrandId,
    remoteComplaintId = complaintId,
    remoteOrderId = orderId,
    issueType = issueType,
    complaintState = complaintState,
    complaintStatus = complaintStatus,
    resolutionStatus = resolution.resolutionStatus,
    items = items.map { it.toComplaintItem(platformBrandId, orderId, complaintId) },
    customerComments = customerComments,
    amountRefunded = resolution.amountRefunded
)

fun ComplaintDataItem.toComplaintItem(
    platformBrandId: Long,
    remoteOrderId: String,
    remoteComplaintId: String,
): ComplaintItem = ComplaintItem(
    platformBrandId = platformBrandId,
    remoteOrderId = remoteOrderId,
    remoteComplaintId = remoteComplaintId,
    remoteItemId = itemId
)
