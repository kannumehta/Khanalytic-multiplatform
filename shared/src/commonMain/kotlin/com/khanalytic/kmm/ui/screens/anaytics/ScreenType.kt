package com.khanalytic.kmm.ui.screens.anaytics

import com.khanalytic.kmm.http.api.AnalyticsReportApi
import com.khanalytic.kmm.http.api.MenuApi
import com.khanalytic.models.MenuItemStats
import org.koin.core.component.KoinComponent

sealed class ScreenType<T>(val api: AnalyticsReportApi<T>): KoinComponent {
    data class Menu(val menuApi: MenuApi): ScreenType<MenuItemStats>(menuApi)
}