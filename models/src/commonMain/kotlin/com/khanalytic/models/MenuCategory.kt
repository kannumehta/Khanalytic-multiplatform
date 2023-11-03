package com.khanalytic.models


import kotlinx.serialization.Serializable

@Serializable
data class MenuCategory(
    val remoteCategoryId: String,
    val name: String,
    val subcategories: List<MenuSubcategory>,
    val order: Int
)