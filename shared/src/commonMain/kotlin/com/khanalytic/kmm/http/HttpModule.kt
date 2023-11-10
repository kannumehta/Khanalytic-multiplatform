package com.khanalytic.kmm.http

import com.khanalytic.database.shared.BrandDao
import com.khanalytic.kmm.http.api.BrandApi
import com.khanalytic.kmm.http.api.PlatformApi
import com.khanalytic.kmm.http.api.UserApi
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val httpModule = module {
    single { HttpClientFactory().create() }
    single { HttpUserPlatformCookieStorageFactory() }
    single { UserApi() }
    single { PlatformApi() }
    single { BrandApi() }
}