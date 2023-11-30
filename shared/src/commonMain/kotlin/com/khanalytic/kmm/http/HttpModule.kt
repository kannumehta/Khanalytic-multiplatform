package com.khanalytic.kmm.http

import com.khanalytic.database.shared.BrandDao
import com.khanalytic.kmm.http.api.BrandApi
import com.khanalytic.kmm.http.api.BusinessReportApi
import com.khanalytic.kmm.http.api.ComplaintApi
import com.khanalytic.kmm.http.api.MenuApi
import com.khanalytic.kmm.http.api.MenuOrderApi
import com.khanalytic.kmm.http.api.MissingDatesApi
import com.khanalytic.kmm.http.api.PlatformApi
import com.khanalytic.kmm.http.api.SalesSummaryApi
import com.khanalytic.kmm.http.api.UserApi
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val httpModule = module {
    single { HttpClientFactory().create() }
    single { HttpUserPlatformCookieStorageFactory() }
    single { UserApi() }
    single { PlatformApi() }
    single { BrandApi() }
    single { MenuApi() }
    single { MissingDatesApi() }
    single { ComplaintApi() }
    single { SalesSummaryApi() }
    single { MenuOrderApi() }
    single { BusinessReportApi() }
}