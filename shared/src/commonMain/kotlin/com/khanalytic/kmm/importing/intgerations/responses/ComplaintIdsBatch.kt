package com.khanalytic.kmm.importing.intgerations.responses

import com.khanalytic.kmm.importing.intgerations.models.Complaint

data class ComplaintIdsBatch(
    val ids: List<String>,
    val nextRequestData: String?
)