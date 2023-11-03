package com.khanalytic.models

import kotlinx.serialization.Serializable

@Serializable
data class MenuItem(
    val remoteItemId: String,
    val name: String,
    val description: String,
    val isVeg: Boolean,
    val isSpicy: Boolean,
    val price: Float,
    val order: Int,
    val isAddOn: Boolean,
    val imageUrls: List<String>,
    val variants: List<MenuItemVariant>,
    val addons: List<MenuItemAddon>
)

@Serializable
data class MenuItemVariant(
    val remoteVariantId: String,
    val name: String,
    val price: Float,
    val active: Boolean
)

@Serializable
data class MenuItemAddon(
    val remoteAddonId: String,
    val active: Boolean
)
