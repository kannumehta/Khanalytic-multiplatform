package com.khanalytic.kmm.ui.screens.anaytics

import com.khanalytic.kmm.http.api.AnalyticsReportApi
import com.khanalytic.kmm.http.api.BusinessReportApi
import com.khanalytic.kmm.http.api.DiscountApi
import com.khanalytic.kmm.http.api.MenuApi
import com.khanalytic.models.DiscountReport
import com.khanalytic.models.MenuItemStats
import com.khanalytic.models.SaleReport
import org.koin.core.component.KoinComponent

sealed class ScreenType<T>(val api: AnalyticsReportApi<T>): KoinComponent {
    data class Menu(val menuApi: MenuApi): ScreenType<MenuItemStats>(menuApi)
    data class BusinessReport(
        val businessReportApi: BusinessReportApi
    ): ScreenType<List<SaleReport>>(businessReportApi)

    data class Discounts(
        val discountApi: DiscountApi
    ): ScreenType<List<DiscountReport>>(discountApi)
}