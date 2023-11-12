package com.khanalytic.models

import kotlinx.serialization.Serializable

@Serializable
data class MenuOrder(
    val platformBrandId: Long,
    val remoteOrderId: String,
    val isDelivered: Boolean,
    val restaurantDiscount: Float,
    val otherDiscount: Float,
    val serviceCharge: Float,
    val gst: Float,
    val orderedTimestamp: String,
    val deliveredTimestamp: String?,
    val packagingCharges: Float,
    val deliveryCharges: Float,
    val items: List<MenuOrderItem>
)

@Serializable
data class MenuOrderItem(
    val platformBrandId: Long,
    val remoteOrderId: String,
    val remoteItemId: String,
    val packagingCharges: Float,
    val subtotal: Float,
    val total: Float,
    val quantity: Int,
    val addons: List<MenuOrderItemAddon>,
    val variants: List<MenuOrderItemVariant>
)

@Serializable
data class MenuOrderItemAddon(
    val platformBrandId: Long,
    val remoteOrderId: String,
    val remoteItemId: String,
    val remoteAddonId: String?,
    val name: String,
)

@Serializable
data class MenuOrderItemVariant(
    val platformBrandId: Long,
    val remoteOrderId: String,
    val remoteItemId: String,
    val remoteVariantId: String?,
    val name: String
)