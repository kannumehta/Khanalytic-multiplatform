package com.khanalytic.kmm.importing.intgerations.responses

import com.khanalytic.kmm.importing.intgerations.models.MenuOrder

data class MenuOrdersBatch(
    val batch: List<MenuOrder>,
    val nextRequestData: String?
)
