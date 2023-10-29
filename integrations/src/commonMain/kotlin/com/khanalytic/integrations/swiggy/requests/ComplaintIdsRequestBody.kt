package com.khanalytic.integrations.swiggy.requests

import kotlinx.serialization.Serializable

fun complaintIdsRequestBody(
    resId: String,
    startDate: String,
    endDate: String,
    nextToken: String? = null
): ComplaintsRequestBody = ComplaintsRequestBody(
    variables = ComplaintIdsVariables(
        complaintStartDate = startDate,
        complaintEndDate = endDate,
        restaurantIds = listOf(resId.toLong()),
        nextToken = nextToken
    )
)

@Serializable
data class ComplaintsRequestBody(
    val operationName: String = "Complaints",
    val query: String = ComplaintIdsConstants.COMPLAINTS_QUERY,
    val variables: ComplaintIdsVariables,
)

@Serializable
data class ComplaintIdsVariables(
    val complaintEndDate: String,
    val complaintResolutionStates: List<String> = ComplaintIdsConstants.resolutionStates,
    val complaintStartDate: String,
    val complaintStates: List<String> = ComplaintIdsConstants.complaintStates,
    val issueTypes: List<String> = ComplaintIdsConstants.issueTypes,
    val limit: Int = 20,
    val nextToken: String? = null,
    val rating: List<Int> = listOf(),
    val restaurantIds: List<Long>,
    val sortBy: String = "RECENT_COMPLAINTS"
)

private object ComplaintIdsConstants {
    // TODO(kannumehta@): Can we build an automated way of getting these configurations?
    val resolutionStates = listOf("GIVEN", "NOT_GIVEN")
    val complaintStates = listOf("UNRESOLVED", "RESOLVED", "EXPIRED")
    val issueTypes = listOf("MISSING_ITEMS", "WRONG_ITEMS", "QUALITY_ISSUES", "QUANTITY_ISSUES",
        "SPILLAGE_ISSUES", "PACKAGING_AND_SPILLAGE_ISSUES", "PACKAGING_ISSUES")
    const val COMPLAINTS_QUERY: String = "\n" +
            "  query Complaints(\n" +
            "    \$restaurantIds: [Int64!]!\n" +
            "    \$complaintStartDate: String\n" +
            "    \$complaintEndDate: String\n" +
            "    \$issueTypes: [String!]\n" +
            "    \$complaintStates: [ComplaintState!]\n" +
            "    \$complaintResolutionStates: [ResolutionStatus!]\n" +
            "    \$limit: Int\n" +
            "    \$nextToken: String\n" +
            "    \$rating: [Int!]\n" +
            "    \$sortBy: String\n" +
            "  ) {\n" +
            "    getComplaints(\n" +
            "      restaurantIds: \$restaurantIds\n" +
            "      complaintStartDate: \$complaintStartDate\n" +
            "      complaintEndDate: \$complaintEndDate\n" +
            "      issueTypes: \$issueTypes\n" +
            "      complaintStates: \$complaintStates\n" +
            "      complaintResolutionStates: \$complaintResolutionStates\n" +
            "      limit: \$limit\n" +
            "      nextToken: \$nextToken\n" +
            "      ratings: \$rating\n" +
            "      sortBy: \$sortBy\n" +
            "    ) {\n" +
            "      complaints {\n" +
            "        restaurantId\n" +
            "        complaintId\n" +
            "        orderId\n" +
            "        customerContextDescription\n" +
            "        issueType\n" +
            "        issueDescription\n" +
            "        createTimestamp\n" +
            "        complaintExpiryTimestamp\n" +
            "        complaintItemDescription\n" +
            "        complaintItemDescriptionV2\n" +
            "        issueL2Disposition\n" +
            "        complaintState\n" +
            "        restaurantResolution {\n" +
            "          resolutionStatus\n" +
            "          amountRefunded\n" +
            "          reasonForNoRefund\n" +
            "          resolutionTime\n" +
            "          resolutionTimestamp\n" +
            "          approvedBy\n" +
            "          resolutionType\n" +
            "        }\n" +
            "        ratings {\n" +
            "          restaurantId\n" +
            "          complaintId\n" +
            "          orderId\n" +
            "          latestRating\n" +
            "          latestRatingEpoch\n" +
            "          oldestRating\n" +
            "          oldestRatingEpoch\n" +
            "          customerName\n" +
            "        }\n" +
            "      }\n" +
            "      nextToken\n" +
            "    }\n" +
            "  }\n"
}