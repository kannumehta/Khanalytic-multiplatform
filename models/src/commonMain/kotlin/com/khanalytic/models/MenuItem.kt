package com.khanalytic.models

import kotlinx.serialization.Serializable

@Serializable
data class MenuItem(
    val platformBrandId: Long,
    val remoteItemId: String,
    val name: String,
    val description: String,
    val isVeg: Boolean,
    val isSpicy: Boolean,
    val price: Float,
    val order: Int,
    val isAddon: Boolean,
    val imageUrls: List<String>,
    val variants: List<MenuItemVariant>,
    val addons: List<MenuItemAddon>,
    val active: Boolean = true
)

@Serializable
data class MenuItemVariant(
    val platformBrandId: Long,
    val remoteItemId: String,
    val remoteVariantId: String,
    val name: String,
    val price: Float,
    val active: Boolean,
    val order: Int
)

@Serializable
data class MenuItemAddon(
    val platformBrandId: Long,
    val remoteItemId: String,
    val remoteAddonId: String,
    val active: Boolean,
    val order: Int,
)
