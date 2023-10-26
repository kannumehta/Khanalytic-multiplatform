package com.khanalytic.kmm.importing.intgerations

import com.khanalytic.kmm.importing.intgerations.models.Menu
import com.khanalytic.kmm.importing.intgerations.models.SalesSummary
import com.khanalytic.kmm.importing.intgerations.swiggy.responses.BrandDetail

interface PlatformApi {

    @Throws(Exception::class)
    suspend fun getBrands(): List<BrandDetail>

    @Throws(Exception::class)
    suspend fun getSalesSummary(resId: String, startDate: String, endDate: String): SalesSummary

    @Throws(Exception::class)
    suspend fun getMenu(resId: String): Menu
}