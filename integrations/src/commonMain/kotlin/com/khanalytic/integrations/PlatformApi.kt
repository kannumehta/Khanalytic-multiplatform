package com.khanalytic.integrations

import com.khanalytic.models.Complaint
import com.khanalytic.models.Menu
import com.khanalytic.models.MenuOrder
import com.khanalytic.models.SalesSummary
import com.khanalytic.models.Brand
import kotlinx.datetime.LocalDate

interface PlatformApi {

    @Throws(Exception::class)
    suspend fun getBrands(
        platformId: Long,
        existingRemoteBrandIdsWithActive: Map<String, Boolean>
    ): List<Brand>

    @Throws(Exception::class)
    suspend fun getSalesSummary(
        platformBrandId: Long,
        resId: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): SalesSummary

    @Throws(Exception::class)
    suspend fun getMenu(platformBrandId: Long, resId: String): Menu

    @Throws(Exception::class)
    suspend fun getOrders(
        platformBrandId: Long,
        resId: String,
        startDate: LocalDate,
        endDate: LocalDate,
        menu: Menu
    ): List<MenuOrder>

    @Throws(Exception::class)
    suspend fun getComplaints(
        platformBrandId: Long,
        resId: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<Complaint>

    @Throws(Exception::class)
    suspend fun sendEmailReport(resId: String, startDate: String, endDate: String, email: String)
}