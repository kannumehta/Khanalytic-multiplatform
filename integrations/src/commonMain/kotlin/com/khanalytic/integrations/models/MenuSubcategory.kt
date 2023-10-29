package com.khanalytic.integrations.models

import kotlinx.serialization.Serializable

@Serializable
data class MenuSubcategory(
    val remoteSubcategoryId: String,
    val name: String,
    val items: List<MenuSubcategoryItem>,
    val order: Int
)

@Serializable
data class MenuSubcategoryItem(
    val remoteItemId: String
)