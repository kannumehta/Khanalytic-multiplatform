package com.khanalytic.kmm.importing.intgerations.responses

import com.khanalytic.kmm.importing.intgerations.models.MenuOrder
import kotlinx.serialization.Serializable

@Serializable
data class MenuOrdersBatch(
    val batch: List<MenuOrder>,
    val nextRequestData: String?
)
