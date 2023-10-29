package com.khanalytic.integrations.models

import kotlinx.serialization.Serializable

@Serializable
data class Menu(
    val categories: List<MenuCategory>,
    val items: List<MenuItem>
)