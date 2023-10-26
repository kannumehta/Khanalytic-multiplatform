package com.khanalytic.kmm.importing.intgerations.swiggy.responses

import com.khanalytic.kmm.importing.intgerations.models.*
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
    val item: Item
)

@Serializable
data class Item(
    val id: Long,
    val name: String,
    val description: String,
    @SerialName("is_veg") val isVeg: Int,
    @SerialName("is_spicy") val isSpicy: Int,
    val price: Float,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("variant_groups_vo") val variantGroups: List<VariantGroup> = listOf(),
    @SerialName("addon_groups_vo") val addonGroups: List<ItemAddonGroup> = listOf(),
)

@Serializable
data class VariantGroup(
    @SerialName("variants_vo") val variants: List<Variant>
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
    @SerialName("addons_vo") val addons: List<ItemAddon>
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

fun MenuResponse.toMenu(): Menu {
    val categories = data.categories.map { it.toMenuCategory() }
    val items = data.items.map { it.item.toMenuItem() }.plus(
        data.addons.map { it.toMenuItem() }
    )
    val finalCategories = if (data.addons.isEmpty()) { categories }
    else {
        val addonCategory = addOnCategory(data.addons.map { it.toMenuSubcategoryItem() })
        categories.plus(addonCategory)
    }
    return Menu(finalCategories, items)
}

fun CategoryData.toMenuCategory(): MenuCategory =
    MenuCategory(
        remoteCategoryId = id.toString(),
        name = name,
        subcategories = menuSubcategories(),
        order = 0
    )

fun CategoryData.menuSubcategories(): List<MenuSubcategory> =
    subcategories.map { it.toMenuSubcategory() }

fun SubcategoryData.toMenuSubcategory(): MenuSubcategory =
    MenuSubcategory(
        remoteSubcategoryId = id.toString(),
        name = name,
        items = menuSubcategoryItems(),
        order = 0
    )

fun SubcategoryData.menuSubcategoryItems(): List<MenuSubcategoryItem> =
    items.map { subcategoryItem: SubcategoryItem ->
        subcategoryItem.toMenuSubcategoryItem()
    }

fun SubcategoryItem.toMenuSubcategoryItem(): MenuSubcategoryItem = MenuSubcategoryItem(
    remoteItemId = id.toString()
)

fun Item.toMenuItem(): MenuItem = MenuItem(
    remoteItemId = id.toString(),
    name = name,
    description = description,
    isVeg = isVeg > 0,
    isSpicy = isSpicy > 0,
    price = price,
    order = 0,
    isAddOn = false,
    imageUrls = imageUrl?.let { listOf(it) } ?: listOf(),
    variants = menuItemVariants(),
    addons = menuItemAddons()
)

fun Item.menuItemVariants(): List<MenuItemVariant> =
    variantGroups.flatMap { it.variants }.map { it.toMenuItemVariant() }

fun Variant.toMenuItemVariant(): MenuItemVariant = MenuItemVariant(
    remoteVariantId = id.toString(),
    name = name,
    price = price,
    active = active
)

fun Item.menuItemAddons(): List<MenuItemAddon> =
    addonGroups.flatMap { it.addons }.map { it.toMenuItemAddon() }

fun ItemAddon.toMenuItemAddon(): MenuItemAddon = MenuItemAddon(
    remoteAddonId = id.toString(),
    active = active
)

fun Addon.toMenuItem(): MenuItem = MenuItem(
    remoteItemId = id.toString(),
    name = name,
    description = "",
    isVeg = false,
    isSpicy = false,
    price = price,
    order = 0,
    isAddOn = true,
    imageUrls = listOf(),
    variants = listOf(),
    addons = listOf()
)

fun Addon.toMenuSubcategoryItem(): MenuSubcategoryItem =
    MenuSubcategoryItem(remoteItemId = id.toString())

fun addOnCategory(items: List<MenuSubcategoryItem>): MenuCategory {
    val name = "Add-ons"
    val menuSubcategory = MenuSubcategory(
        remoteSubcategoryId = name,
        items = items,
        name = name,
        order = 0,
    )
    return MenuCategory(
        remoteCategoryId = name,
        name = name,
        order = 0,
        subcategories = listOf(menuSubcategory)
    )
}