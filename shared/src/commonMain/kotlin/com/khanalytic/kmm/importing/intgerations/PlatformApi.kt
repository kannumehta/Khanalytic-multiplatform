package com.khanalytic.kmm.importing.intgerations

import com.khanalytic.kmm.importing.intgerations.models.SalesSummary
import com.khanalytic.kmm.importing.intgerations.swiggy.responses.BrandDetail
import kotlinx.datetime.LocalDate

interface PlatformApi {

    @Throws(Exception::class)
    suspend fun getBrands(): List<BrandDetail>

    @Throws(Exception::class)
    suspend fun getSalesSummary(resId: String, startDate: String, endDate: String): SalesSummary
}