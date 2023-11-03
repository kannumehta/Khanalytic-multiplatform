package com.khanalytic.integrations.responses

import com.khanalytic.models.MenuOrder

data class MenuOrdersBatch(
    val batch: List<MenuOrder>,
    val nextRequestData: String?
)
