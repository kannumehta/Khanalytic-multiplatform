package com.khanalytic.kmm.ui.screens.filter

import com.khanalytic.models.Brand
import com.khanalytic.models.Platform

sealed class ListItem {
    data class Header(val name: String): ListItem()
    data class BrandItem(val brand: Brand, val selected: Boolean): ListItem()
    data class PlatformItem (val platform: Platform, val selected: Boolean): ListItem()

    data object ReportTypeItem: ListItem()
}