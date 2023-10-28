package com.khanalytic.kmm.importing.intgerations

import com.khanalytic.kmm.importing.intgerations.models.Complaint
import com.khanalytic.kmm.importing.intgerations.models.Menu
import com.khanalytic.kmm.importing.intgerations.models.SalesSummary
import com.khanalytic.kmm.importing.intgerations.responses.ComplaintIdsBatch
import com.khanalytic.kmm.importing.intgerations.responses.MenuOrdersBatch
import com.khanalytic.kmm.importing.intgerations.swiggy.responses.BrandDetail

interface PlatformResponseParser {
    @Throws(Exception::class) fun parseBrandIds(response: String): List<String>
    @Throws(Exception::class) fun parseBrand(response: String): BrandDetail
    @Throws(Exception::class) fun parseSalesSummary(response: String): SalesSummary
    @Throws(Exception::class) fun parseMenu(response: String): Menu
    @Throws(Exception::class) fun parseOrders(response: String, menu: Menu): MenuOrdersBatch
    @Throws(Exception::class) fun parseComplaintIdsBatch(response: String): ComplaintIdsBatch
    @Throws(Exception::class) fun parseComplaint(response: String): Complaint
}