package com.khanalytic.kmm.importing.intgerations.swiggy.requests

import kotlinx.serialization.Serializable

fun sendEmailReportRequestBody(
    resId: String,
    startDate: String,
    endDate: String,
    email: String
): SendEmailReportRequestBody = SendEmailReportRequestBody(
    variables = SendEmailReportVariables(
        email = email,
        fromDate = startDate,
        toDate = endDate,
        rids = listOf(resId.toLong())
    )
)

@Serializable
data class SendEmailReportRequestBody(
    val operationName: String = "DownloadFinanceReport",
    val query: String = SendEmailReportConstants.SEND_EMAIL_REPORT_QUERY,
    val variables: SendEmailReportVariables,
)

@Serializable
data class SendEmailReportVariables(
    val email: String,
    val fromDate: String,
    val toDate: String,
    val rids: List<Long>,
    val type: String = "CONSOLIDATED_ANNEXURE"
)

private object SendEmailReportConstants {
    const val SEND_EMAIL_REPORT_QUERY: String = "query DownloadFinanceReport(\$rids: [Int64!]!, \$type: String!, \$fromDate: String!, \$toDate: String!, \$email: String!) {\n" +
            "  downloadFinanceReport(\n" +
            "    input: {rids: \$rids, fromDate: \$fromDate, toDate: \$toDate, email: \$email, type: \$type}\n" +
            "  ) {\n" +
            "    statusCode\n" +
            "    message\n" +
            "    __typename\n" +
            "  }\n" +
            "}\n"
}