package com.khanalytic.integrations.swiggy.responses

import com.khanalytic.models.Menu
import com.khanalytic.models.MenuItem
import com.khanalytic.models.MenuItemVariant
import com.khanalytic.models.MenuOrder
import com.khanalytic.models.MenuOrderItem
import com.khanalytic.models.MenuOrderItemAddon
import com.khanalytic.models.MenuOrderItemVariant
import com.khanalytic.integrations.responses.MenuOrdersBatch
import com.khanalytic.integrations.swiggy.DataParseException
import com.khanalytic.integrations.Serialization
import io.ktor.util.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonElement

@Serializable
data class MenuOrderResponseData(
    @SerialName("data") val data: List<JsonElement>
)

@Serializable(with = MenuResponseDeSerializer::class)
data class MenuOrderResponse(
    @SerialName("data") val outerData: OrdersOuterData
)

@Serializable
data class OrdersOuterData(
    @SerialName("data") val innerData: OrdersInnerData
)

@Serializable
data class OrdersInnerData(
    val meta: OrdersMetaData,
    @SerialName("objects") val orders: List<Order>
)

@Serializable
data class OrdersMetaData(
    val next: String? = null
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
    @SerialName("delivered_time") val deliveredTime: String?,
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

fun MenuOrderResponse.toMenuOrdersBatch(platformBrandId: Long, menu: Menu): MenuOrdersBatch =
    MenuOrdersBatch(
        toOrders(platformBrandId, menu),
        outerData.innerData.meta.next
    )

fun MenuOrderResponse.toOrders(platformBrandId: Long, menu: Menu): List<MenuOrder> {
    val addonNameMap = menu.items.filter { it.isAddon  }.associateBy { it.name }
    val itemIdMap = menu.items.filterNot { it.isAddon  }.associateBy { it.remoteItemId }
    return outerData.innerData.orders.map {
        it.toMenuOrder(platformBrandId, addonNameMap, itemIdMap)
    }
}

private class MenuResponseDeSerializer: KSerializer<MenuOrderResponse> {
    private val serializer = Serialization.serializer
    override val descriptor: SerialDescriptor = MenuOrderResponseData.serializer().descriptor
    override fun serialize(encoder: Encoder, value: MenuOrderResponse) {
        throw NotImplementedError("serialization of menu order response should not be needed")
    }

    override fun deserialize(decoder: Decoder): MenuOrderResponse {
        val menuResponseData = decoder.decodeSerializableValue(MenuOrderResponseData.serializer())
        val dataList = menuResponseData.data
        if (dataList.isEmpty()) {
            throw DataParseException("could not extract data from orders history response")
        }
        return MenuOrderResponse(serializer.decodeFromString(dataList[0].toString()))
    }
}

private fun Order.toMenuOrder(
    platformBrandId: Long,
    addonNameMap: Map<String, MenuItem>,
    itemIdMap: Map<String, MenuItem>,
): MenuOrder = MenuOrder(
    platformBrandId = platformBrandId,
    remoteOrderId = id,
    isDelivered = status.status.toLowerCasePreservingASCIIRules() == "delivered",
    restaurantDiscount = restaurantDiscount,
    otherDiscount = otherDiscount,
    serviceCharge = serviceCharge,
    gst = gst,
    orderedTimestamp = status.orderedTime,
    deliveredTimestamp = status.deliveredTime,
    packagingCharges = cart.charges.packingCharge,
    deliveryCharges = cart.charges.deliveryCharge,
    items = cart.items.map { it.toMenuOrderItem(platformBrandId, id, addonNameMap, itemIdMap) }
)

private fun CartItem.toMenuOrderItem(
    platformBrandId: Long,
    remoteOrderId: String,
    addonNameMap: Map<String, MenuItem>,
    itemIdMap: Map<String, MenuItem>
) : MenuOrderItem {
    val variantNameMap = itemIdMap[id.toString()]?.variants?.associateBy { it.name } ?: mapOf()
    val filteredAddons =
        addons.map {
            it.toMenuOrderItemAddon(platformBrandId, remoteOrderId, id.toString(), addonNameMap)
        }.let {
            it.filter { addon ->
                addon.remoteAddonId != null || !variantNameMap.contains(addon.name)
            }
        }
    return MenuOrderItem(
        platformBrandId = platformBrandId,
        remoteOrderId = remoteOrderId,
        remoteItemId = id.toString(),
        packagingCharges = packagingCharges,
        subtotal = subTotal,
        total = total,
        quantity = quantity,
        addons = filteredAddons,
        variants = variants.map {
            it.toMenuOrderItemVariant(platformBrandId, remoteOrderId, id.toString(), variantNameMap)
        }
    )
}

private fun CartItemAddon.toMenuOrderItemAddon(
    platformBrandId: Long,
    remoteOrderId: String,
    remoteItemId: String,
    addonNameMap: Map<String, MenuItem>
): MenuOrderItemAddon = MenuOrderItemAddon(
    platformBrandId = platformBrandId,
    remoteOrderId = remoteOrderId,
    remoteItemId = remoteItemId,
    remoteAddonId = addonNameMap[name]?.remoteItemId,
    name = name
)

private fun CartItemVariant.toMenuOrderItemVariant(
    platformBrandId: Long,
    remoteOrderId: String,
    remoteItemId: String,
    variantNameMap: Map<String, MenuItemVariant>
): MenuOrderItemVariant = MenuOrderItemVariant(
    platformBrandId = platformBrandId,
    remoteOrderId = remoteOrderId,
    remoteItemId = remoteItemId,
    remoteVariantId = variantNameMap[name]?.remoteVariantId,
    name = name
)


