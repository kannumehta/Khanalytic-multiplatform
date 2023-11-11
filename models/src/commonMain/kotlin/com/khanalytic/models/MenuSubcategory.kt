package com.khanalytic.models

import kotlinx.serialization.Serializable

@Serializable
data class MenuSubcategory(
    val platformBrandId: Long,
    val remoteCategoryId: String,
    val remoteSubcategoryId: String,
    val name: String,
    val items: List<MenuSubcategoryItem>,
    val order: Int,
    val active: Boolean = true
)

@Serializable
data class MenuSubcategoryItem(
    val platformBrandId: Long,
    val remoteSubcategoryId: String,
    val remoteItemId: String,
    val order: Int,
)