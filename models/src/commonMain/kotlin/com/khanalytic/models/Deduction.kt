package com.khanalytic.models

import kotlinx.serialization.Serializable

@Serializable
data class Deduction(
    val deductionType: String,
    val totalAmount: Float
)