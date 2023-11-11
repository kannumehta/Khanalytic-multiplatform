package com.khanalytic.integrations.swiggy.responses

import com.khanalytic.models.Menu
import com.khanalytic.models.MenuCategory
import com.khanalytic.models.MenuItem
import com.khanalytic.models.MenuItemAddon
import com.khanalytic.models.MenuItemVariant
import com.khanalytic.models.MenuSubcategory
import com.khanalytic.models.MenuSubcategoryItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuResponse(
    val data: MenuItemsData
)

@Serializable
data class MenuItemsData(
    @SerialName("categories_order") val categories: List<CategoryData>,
    @SerialName("items_vo") val items: List<ItemData>,
    @SerialName("addons") val addons: List<Addon>
)

@Serializable
data class CategoryData(
    val id: Long,
    val name: String,
    @SerialName("sub_categories_order") val subcategories: List<SubcategoryData>
)

@Serializable
data class SubcategoryData(
    val id: Long,
    val name: String,
    @SerialName("items_order") val items: List<SubcategoryItem>
)

@Serializable
data class SubcategoryItem(val id: Long)

@Serializable
data class ItemData(
    val item: Item,
    @SerialName("variant_groups_vo") val variantGroups: List<VariantGroup> = listOf(),
    @SerialName("addon_groups_vo") val addonGroups: List<ItemAddonGroup> = listOf(),
)

@Serializable
data class Item(
    val id: Long,
    val name: String,
    val description: String,
    @SerialName("is_veg") val isVeg: Int,
    @SerialName("is_spicy") val isSpicy: Int,
    val price: Float,
    @SerialName("image_url") val imageUrl: String? = null
)

@Serializable
data class VariantGroup(
    @SerialName("variants_vo") val variants: List<VariantData>
)

@Serializable
data class VariantData(
    val variant: Variant
)

@Serializable
data class Variant(
    val id: Long,
    val name: String,
    val price: Float,
    val active: Boolean
)

@Serializable
data class ItemAddonGroup(
    @SerialName("addons_vo") val addons: List<ItemAddonData>
)

@Serializable
data class ItemAddonData(
    val addon: ItemAddon
)

@Serializable
data class ItemAddon(
    val id: Long,
    val active: Boolean
)

@Serializable
data class Addon(
    val id: Long,
    val name: String,
    val price: Float,
    val active: Boolean
)

fun MenuResponse.toMenu(platformBrandId: Long): Menu {
    val categories = data.categories.map { it.toMenuCategory(platformBrandId) }
    val items = data.items.map { it.toMenuItem(platformBrandId) }.plus(
        data.addons.map { it.toMenuItem(platformBrandId) }
    )
    val finalCategories = if (data.addons.isEmpty()) { categories }
    else {
        val addonCategory = addOnCategory(
            platformBrandId,
            data.addons.map { it.toMenuSubcategoryItem(platformBrandId, ADDONS_NAME) }
        )
        categories.plus(addonCategory)
    }
    return Menu(finalCategories, items)
}

fun CategoryData.toMenuCategory(platformBrandId: Long): MenuCategory =
    MenuCategory(
        platformBrandId = platformBrandId,
        remoteCategoryId = id.toString(),
        name = name,
        subcategories = menuSubcategories(platformBrandId, id.toString()),
        order = 0
    )

fun CategoryData.menuSubcategories(
    platformBrandId: Long,
    remoteCategoryId: String,
): List<MenuSubcategory> =
    subcategories.map { it.toMenuSubcategory(platformBrandId, remoteCategoryId) }

fun SubcategoryData.toMenuSubcategory(
    platformBrandId: Long,
    remoteCategoryId: String
): MenuSubcategory =
    MenuSubcategory(
        platformBrandId = platformBrandId,
        remoteCategoryId = remoteCategoryId,
        remoteSubcategoryId = id.toString(),
        name = name,
        items = menuSubcategoryItems(platformBrandId, id.toString()),
        order = 0
    )

fun SubcategoryData.menuSubcategoryItems(
    platformBrandId: Long,
    remoteSubcategoryId: String
): List<MenuSubcategoryItem> =
    items.map { subcategoryItem: SubcategoryItem ->
        subcategoryItem.toMenuSubcategoryItem(platformBrandId, remoteSubcategoryId)
    }

fun SubcategoryItem.toMenuSubcategoryItem(
    platformBrandId: Long,
    remoteSubcategoryId: String
): MenuSubcategoryItem = MenuSubcategoryItem(
    platformBrandId = platformBrandId,
    remoteSubcategoryId = remoteSubcategoryId,
    remoteItemId = id.toString(),
    order = 0,
)

fun ItemData.toMenuItem(
    platformBrandId: Long
): MenuItem = MenuItem(
    platformBrandId = platformBrandId,
    remoteItemId = item.id.toString(),
    name = item.name,
    description = item.description,
    isVeg = item.isVeg > 0,
    isSpicy = item.isSpicy > 0,
    price = item.price,
    order = 0,
    isAddon = false,
    imageUrls = item.imageUrl?.let { listOf(it) } ?: listOf(),
    variants = menuItemVariants(platformBrandId, item.id.toString()),
    addons = menuItemAddons(platformBrandId, item.id.toString())
)

fun ItemData.menuItemVariants(
    platformBrandId: Long,
    remoteItemId: String
): List<MenuItemVariant> = variantGroups.flatMap { it.variants }.map {
    it.variant.toMenuItemVariant(platformBrandId, remoteItemId)
}

fun Variant.toMenuItemVariant(
    platformBrandId: Long,
    remoteItemId: String
): MenuItemVariant = MenuItemVariant(
    platformBrandId = platformBrandId,
    remoteItemId = remoteItemId,
    remoteVariantId = id.toString(),
    name = name,
    price = price,
    active = active,
    order = 0
)

fun ItemData.menuItemAddons(
    platformBrandId: Long,
    remoteItemId: String
): List<MenuItemAddon> =
    addonGroups.flatMap { it.addons }.map { it.addon.toMenuItemAddon(platformBrandId, remoteItemId) }

fun ItemAddon.toMenuItemAddon(
    platformBrandId: Long,
    remoteItemId: String
): MenuItemAddon = MenuItemAddon(
    platformBrandId = platformBrandId,
    remoteItemId = remoteItemId,
    remoteAddonId = id.toString(),
    active = active,
    order = 0
)

fun Addon.toMenuItem(
    platformBrandId: Long
): MenuItem = MenuItem(
    platformBrandId = platformBrandId,
    remoteItemId = id.toString(),
    name = name,
    description = "",
    isVeg = false,
    isSpicy = false,
    price = price,
    order = 0,
    isAddon = true,
    imageUrls = listOf(),
    variants = listOf(),
    addons = listOf()
)

fun Addon.toMenuSubcategoryItem(
    platformBrandId: Long,
    remoteSubcategoryId: String
): MenuSubcategoryItem =
    MenuSubcategoryItem(
        platformBrandId = platformBrandId,
        remoteItemId = id.toString(),
        remoteSubcategoryId = remoteSubcategoryId,
        order = 0
    )

fun addOnCategory(
    platformBrandId: Long,
    items: List<MenuSubcategoryItem>
): MenuCategory {
    val menuSubcategory = MenuSubcategory(
        platformBrandId = platformBrandId,
        remoteCategoryId = ADDONS_NAME,
        remoteSubcategoryId = ADDONS_NAME,
        items = items,
        name = ADDONS_NAME,
        order = 0,
    )
    return MenuCategory(
        platformBrandId = platformBrandId,
        remoteCategoryId = ADDONS_NAME,
        name = ADDONS_NAME,
        order = 0,
        subcategories = listOf(menuSubcategory)
    )
}

private const val ADDONS_NAME = "Add-ons"