package com.khanalytic.integrations

import com.khanalytic.models.Complaint
import com.khanalytic.models.Menu
import com.khanalytic.models.SalesSummary
import com.khanalytic.integrations.responses.ComplaintIdsBatch
import com.khanalytic.integrations.responses.MenuOrdersBatch
import com.khanalytic.integrations.swiggy.responses.BrandDetail

interface PlatformResponseParser {
    @Throws(Exception::class) fun parseBrandIds(response: String): List<String>
    @Throws(Exception::class) fun parseBrand(response: String): BrandDetail
    @Throws(Exception::class) fun parseSalesSummary(response: String): SalesSummary
    @Throws(Exception::class) fun parseMenu(response: String): Menu
    @Throws(Exception::class) fun parseOrders(response: String, menu: Menu): MenuOrdersBatch
    @Throws(Exception::class) fun parseComplaintIdsBatch(response: String): ComplaintIdsBatch
    @Throws(Exception::class) fun parseComplaint(response: String): Complaint
}