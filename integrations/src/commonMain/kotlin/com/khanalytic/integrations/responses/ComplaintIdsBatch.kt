package com.khanalytic.integrations.responses

data class ComplaintIdsBatch(
    val ids: List<String>,
    val nextRequestData: String?
)