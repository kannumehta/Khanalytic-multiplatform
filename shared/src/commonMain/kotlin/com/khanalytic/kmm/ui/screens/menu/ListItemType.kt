package com.khanalytic.kmm.ui.screens.menu

import com.khanalytic.models.MenuCategory
import com.khanalytic.models.MenuItem
import com.khanalytic.models.MenuItemStat

sealed class ListItemType {
    data class Category(val menuCategory: MenuCategory): ListItemType()
    data class Item(val menuItem: MenuItem): ListItemType()
}