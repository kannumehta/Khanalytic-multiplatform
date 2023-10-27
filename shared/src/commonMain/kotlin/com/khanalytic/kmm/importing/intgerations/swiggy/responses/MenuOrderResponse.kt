package com.khanalytic.kmm.importing.intgerations.swiggy.responses

import com.khanalytic.kmm.importing.intgerations.models.*
import io.ktor.util.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuOrderResponse(
    @SerialName("data") val outerDataList: List<OrdersOuterData>
)

@Serializable
data class OrdersOuterData(
    @SerialName("data") val innerData: OrdersInnerData
)

@Serializable
data class OrdersInnerData(
    @SerialName("objects") val orders: List<Order>
)

@Serializable
data class Order(
    @SerialName("order_id") val id: String,
    @SerialName("status") val status: OrderStatus,
    @SerialName("restaurant_offers_discount") val restaurantDiscount: Float,
    @SerialName("restaurant_trade_discount") val otherDiscount: Float,
    @SerialName("serviceCharge") val serviceCharge: Float,
    @SerialName("gst") val gst: Float,
    val cart: Cart
)

// TODO(kannumehta@): Make the date time string serialize to ZonedDatetime.
@Serializable
data class OrderStatus(
    @SerialName("order_status") val status: String,
    @SerialName("ordered_time") val orderedTime: String,
    @SerialName("delivered_time") val deliveredTime: String,
)

@Serializable
data class Cart(
    val charges: CartCharges,
    val items: List<CartItem>
)

@Serializable
data class CartCharges(
    @SerialName("packing_charge") val packingCharge: Float,
    @SerialName("delivery_charge") val deliveryCharge: Float,
)

@Serializable
data class CartItem(
    @SerialName("item_id") val id: Long,
    @SerialName("packing_charges") val packagingCharges: Float,
    @SerialName("sub_total") val subTotal: Float,
    val total: Float,
    val quantity: Int,
    val addons: List<CartItemAddon>,
    val variants: List<CartItemVariant>,
)

@Serializable
data class CartItemAddon(
    val name: String
)

@Serializable
data class CartItemVariant(
    val name: String
)

fun MenuOrderResponse.toOrders(menu: Menu): List<MenuOrder> {
    val addonNameMap = menu.items.filter { it.isAddOn  }.associateBy { it.name }
    val itemIdMap = menu.items.filterNot { it.isAddOn  }.associateBy { it.remoteItemId }
    return outerDataList.flatMap { it.innerData.orders }.map {
        it.toMenuOrder(addonNameMap, itemIdMap)
    }
}

private fun Order.toMenuOrder(
    addonNameMap: Map<String, MenuItem>,
    itemIdMap: Map<String, MenuItem>,
): MenuOrder = MenuOrder(
    remoteOrderId = id,
    isDelivered = status.status.toLowerCasePreservingASCIIRules() == "delivered",
    restaurantDiscount = restaurantDiscount,
    otherDiscount = otherDiscount,
    serviceCharge = serviceCharge,
    gst = gst,
    orderedTimestamp = status.orderedTime,
    deliveredTimestamp = status.deliveredTime,
    packingCharges = cart.charges.packingCharge,
    deliveryCharges = cart.charges.deliveryCharge,
    items = cart.items.map { it.toMenuOrderItem(addonNameMap, itemIdMap) }
)

private fun CartItem.toMenuOrderItem(
    addonNameMap: Map<String, MenuItem>,
    itemIdMap: Map<String, MenuItem>
) : MenuOrderItem  {
    val variantNameMap = itemIdMap[id.toString()]?.variants?.associateBy { it.name } ?: mapOf()
    val filteredAddons = addons.map { it.toMenuOrderItemAddon(addonNameMap) }.let {
        it.filter { addon -> addon.remoteAddonId != null || !variantNameMap.contains(addon.name) }
    }
    return MenuOrderItem(
        remoteItemId = id.toString(),
        packagingCharges = packagingCharges,
        subTotal = subTotal,
        total = total,
        quantity = quantity,
        addons = filteredAddons,
        variants = variants.map { it.toMenuOrderItemVariant(variantNameMap) }
    )
}

private fun CartItemAddon.toMenuOrderItemAddon(
    addonNameMap: Map<String, MenuItem>
): MenuOrderItemAddon = MenuOrderItemAddon(
    remoteAddonId = addonNameMap[name]?.remoteItemId,
    name = name
)

private fun CartItemVariant.toMenuOrderItemVariant(
    variantNameMap: Map<String, MenuItemVariant>
): MenuOrderItemVariant = MenuOrderItemVariant(
    remoteVariantId = variantNameMap[name]?.remoteVariantId,
    name = name
)

