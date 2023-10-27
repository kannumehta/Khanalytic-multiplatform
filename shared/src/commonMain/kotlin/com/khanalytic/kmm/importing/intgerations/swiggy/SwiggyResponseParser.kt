package com.khanalytic.kmm.importing.intgerations.swiggy

import com.khanalytic.kmm.importing.intgerations.PlatformResponseParser
import com.khanalytic.kmm.importing.intgerations.models.Menu
import com.khanalytic.kmm.importing.intgerations.models.MenuOrder
import com.khanalytic.kmm.importing.intgerations.models.SalesSummary
import com.khanalytic.kmm.importing.intgerations.responses.MenuOrdersBatch
import com.khanalytic.kmm.importing.intgerations.swiggy.responses.*
import kotlinx.serialization.json.Json

class SwiggyResponseParser(private val serializer: Json) : PlatformResponseParser {
    override fun parseBrandIds(response: String): List<String> {
        val regex = Regex(".*globals.userInfoDataParam\\s*=\\s*(.*?);.*")
        return regex.find(response)?.groups?.get(1)?.value?.let { json: String ->
            serializer.decodeFromString<BrandsResponse>(json).outlets.map { it.resId.toString() }
        } ?: throw DataParseException("failed to extract brands list")
    }

    @Throws(Exception::class) override fun parseBrand(response: String): BrandDetail =
        serializer.decodeFromString<BrandDetailResponse>(response).data

    @Throws(Exception::class) override fun parseSalesSummary(response: String): SalesSummary =
        serializer.decodeFromString<SalesSummaryResponse>(response)
            .data.orderMetricsV4.summary.toSalesSummary()

    @Throws(Exception::class) override fun parseMenu(response: String): Menu =
        serializer.decodeFromString<MenuResponse>(response).toMenu()

    @Throws(Exception::class) override fun parseOrders(
        response: String,
        menu: Menu
    ): MenuOrdersBatch =
        serializer.decodeFromString<MenuOrderResponse>(response).toMenuOrdersBatch(menu)
}