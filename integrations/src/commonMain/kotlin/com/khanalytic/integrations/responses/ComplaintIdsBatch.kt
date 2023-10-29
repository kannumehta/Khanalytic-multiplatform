package com.khanalytic.integrations.responses

import com.khanalytic.integrations.models.Complaint

data class ComplaintIdsBatch(
    val ids: List<String>,
    val nextRequestData: String?
)