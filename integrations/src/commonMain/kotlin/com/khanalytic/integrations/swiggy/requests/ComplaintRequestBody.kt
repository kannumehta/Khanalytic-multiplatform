package com.khanalytic.integrations.swiggy.requests

import kotlinx.serialization.Serializable

fun complaintRequestBody(
    complaintId: String
): ComplaintRequestBody = ComplaintRequestBody(
    variables = ComplaintVariables(
        complaintId = complaintId
    )
)

@Serializable
data class ComplaintRequestBody(
    val operationName: String = "ComplaintOrderDetails",
    val query: String = ComplaintConstants.COMPLAINT_QUERY,
    val variables: ComplaintVariables,
)

@Serializable
data class ComplaintVariables(
    val complaintId: String
)

private object ComplaintConstants {
    const val COMPLAINT_QUERY: String = "\n" +
            "  query ComplaintOrderDetails(\$complaintId: String!) {\n" +
            "    getComplaintOrderDetails(complaintId: \$complaintId) {\n" +
            "      complaintId\n" +
            "      orderId\n" +
            "      issueType\n" +
            "      issueSubType\n" +
            "      issueDescription\n" +
            "      complaintState\n" +
            "      complaintStatus\n" +
            "      complaintExpiryTimestamp\n" +
            "      complaintItemDescription\n" +
            "      issueL2Disposition\n" +
            "      complaintItemDescriptionV2\n" +
            "      extraNudgeEnabled\n" +
            "      rcaInputs {\n" +
            "        itemId\n" +
            "        addonIds\n" +
            "        variantIds\n" +
            "        isSideItemRca\n" +
            "        issueItemId\n" +
            "        complaintItemDescription\n" +
            "        complaintItemDescriptionSubText\n" +
            "        rcaOptions {\n" +
            "          id\n" +
            "          description\n" +
            "          detailedDescription\n" +
            "        }\n" +
            "      }\n" +
            "      rootCauses {\n" +
            "        itemId\n" +
            "        addonIds\n" +
            "        variantIds\n" +
            "        isSideItemRca\n" +
            "        complaintItemDescription\n" +
            "        complaintItemDescriptionSubText\n" +
            "        capturedRootCause {\n" +
            "          rootCauseId\n" +
            "          rootCauseDescription\n" +
            "          rootCauseDetailedDescription\n" +
            "          comments\n" +
            "          capturedAt\n" +
            "        }\n" +
            "      }\n" +
            "      aggregatedItemDescription {\n" +
            "        issueSubType\n" +
            "        itemsDescription\n" +
            "      }\n" +
            "      images {\n" +
            "        link\n" +
            "      }\n" +
            "      recommendedResolution {\n" +
            "        gratificationId\n" +
            "        amountRecommended\n" +
            "        otherOptionsResponse {\n" +
            "          customAmountMinLimit\n" +
            "          customAmountMaxLimit\n" +
            "          partialRefundOptions {\n" +
            "            amount\n" +
            "            percentage\n" +
            "          }\n" +
            "          shouldShowNoResolution\n" +
            "        }\n" +
            "      }\n" +
            "      restaurantResolution {\n" +
            "        amountRefunded\n" +
            "        reasonForPartialRefund\n" +
            "        reasonForNoRefund\n" +
            "        resolutionProvider\n" +
            "        resolutionTime\n" +
            "        resolutionTimestamp\n" +
            "        resolutionStatus\n" +
            "        resolutionType\n" +
            "      }\n" +
            "      customerInfo {\n" +
            "        name\n" +
            "        phoneNumber\n" +
            "        totalComplaints\n" +
            "        totalAmount\n" +
            "        totalOrders\n" +
            "        type\n" +
            "      }\n" +
            "      customerComments\n" +
            "      isAutoDebitableOnExpiry\n" +
            "      ratings {\n" +
            "        restaurantId\n" +
            "        complaintId\n" +
            "        orderId\n" +
            "        latestRating\n" +
            "        latestRatingEpoch\n" +
            "        oldestRating\n" +
            "        oldestRatingEpoch\n" +
            "        customerName\n" +
            "      }\n" +
            "      complaintCategory\n" +
            "      complaintSubCategory\n" +
            "      isCustomerAlreadyGratified\n" +
            "      rxCxCallLogs {\n" +
            "        callLogTitle\n" +
            "        callLogDetails\n" +
            "        render\n" +
            "      }\n" +
            "      complaintDetailFooter\n" +
            "      createTimestamp\n" +
            "      orderDetails {\n" +
            "        serviceCharge\n" +
            "        deliveryCharge\n" +
            "        packaging_charges\n" +
            "        status {\n" +
            "          delivered_time\n" +
            "          ordered_time\n" +
            "          order_status\n" +
            "        }\n" +
            "        cart {\n" +
            "          items {\n" +
            "            item_id\n" +
            "            is_oos\n" +
            "            is_veg\n" +
            "            name\n" +
            "            total\n" +
            "            category\n" +
            "            sub_total\n" +
            "            sub_category\n" +
            "            quantity\n" +
            "            reward_type\n" +
            "            restaurant_discount_hit\n" +
            "            variants {\n" +
            "              name\n" +
            "            }\n" +
            "            addons {\n" +
            "              name\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "        tax\n" +
            "        restaurant_taxation_type\n" +
            "        restaurant_trade_discount\n" +
            "        restaurant_offers_discount\n" +
            "        bill\n" +
            "        gstDetails {\n" +
            "          cart_CGST\n" +
            "          cart_SGST\n" +
            "          packaging_CGST\n" +
            "          packaging_SGST\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }\n"
}