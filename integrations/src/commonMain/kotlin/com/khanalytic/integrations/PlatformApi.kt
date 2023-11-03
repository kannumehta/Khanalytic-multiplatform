package com.khanalytic.integrations

import com.khanalytic.models.Complaint
import com.khanalytic.models.Menu
import com.khanalytic.models.MenuOrder
import com.khanalytic.models.SalesSummary
import com.khanalytic.integrations.swiggy.responses.BrandDetail

interface PlatformApi {

    @Throws(Exception::class)
    suspend fun getBrands(): List<BrandDetail>

    @Throws(Exception::class)
    suspend fun getSalesSummary(resId: String, startDate: String, endDate: String): SalesSummary

    @Throws(Exception::class)
    suspend fun getMenu(resId: String): Menu

    @Throws(Exception::class)
    suspend fun getOrders(
        resId: String,
        startDate: String,
        endDate: String,
        menu: Menu
    ): List<MenuOrder>

    @Throws(Exception::class)
    suspend fun getComplaints(resId: String, startDate: String, endDate: String): List<Complaint>

    @Throws(Exception::class)
    suspend fun sendEmailReport(resId: String, startDate: String, endDate: String, email: String)
}