package com.khanalytic.kmm.importing.intgerations

import com.khanalytic.kmm.importing.intgerations.models.SalesSummary
import com.khanalytic.kmm.importing.intgerations.swiggy.responses.BrandDetail

interface PlatformResponseParser {
    @Throws(Exception::class) fun parseBrandIds(response: String): List<String>
    @Throws(Exception::class) fun parseBrand(response: String): BrandDetail
    @Throws(Exception::class) fun parseSalesSummary(response: String): SalesSummary
}