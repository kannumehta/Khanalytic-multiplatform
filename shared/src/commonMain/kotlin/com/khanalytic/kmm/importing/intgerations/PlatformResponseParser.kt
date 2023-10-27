package com.khanalytic.kmm.importing.intgerations

import com.khanalytic.kmm.importing.intgerations.models.Menu
import com.khanalytic.kmm.importing.intgerations.models.MenuOrder
import com.khanalytic.kmm.importing.intgerations.models.SalesSummary
import com.khanalytic.kmm.importing.intgerations.swiggy.responses.BrandDetail
import com.khanalytic.kmm.importing.intgerations.swiggy.responses.MenuOrderResponse

interface PlatformResponseParser {
    @Throws(Exception::class) fun parseBrandIds(response: String): List<String>
    @Throws(Exception::class) fun parseBrand(response: String): BrandDetail
    @Throws(Exception::class) fun parseSalesSummary(response: String): SalesSummary
    @Throws(Exception::class) fun parseMenu(response: String): Menu
    @Throws(Exception::class) fun parseOrders(response: String, menu: Menu): List<MenuOrder>
}