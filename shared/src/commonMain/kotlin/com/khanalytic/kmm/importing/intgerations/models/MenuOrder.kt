package com.khanalytic.kmm.importing.intgerations.models

import kotlinx.serialization.Serializable

@Serializable
data class MenuOrder(
    val remoteOrderId: String,
    val isDelivered: Boolean,
    val restaurantDiscount: Float,
    val otherDiscount: Float,
    val serviceCharge: Float,
    val gst: Float,
    val orderedTimestamp: String,
    val deliveredTimestamp: String,
    val packingCharges: Float,
    val deliveryCharges: Float,
    val items: List<MenuOrderItem>
)

@Serializable
data class MenuOrderItem(
    val remoteItemId: String,
    val packagingCharges: Float,
    val subTotal: Float,
    val total: Float,
    val quantity: Int,
    val addons: List<MenuOrderItemAddon>,
    val variants: List<MenuOrderItemVariant>
)

@Serializable
data class MenuOrderItemAddon(
    val remoteAddonId: String?,
    val name: String,
)

@Serializable
data class MenuOrderItemVariant(
    val remoteVariantId: String?,
    val name: String
)