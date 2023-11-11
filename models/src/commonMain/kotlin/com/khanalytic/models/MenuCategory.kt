package com.khanalytic.models


import kotlinx.serialization.Serializable

@Serializable
data class MenuCategory(
    val platformBrandId: Long,
    val remoteCategoryId: String,
    val name: String,
    val subcategories: List<MenuSubcategory>,
    val order: Int,
    val active: Boolean = true
)