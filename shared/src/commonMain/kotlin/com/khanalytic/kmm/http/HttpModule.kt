package com.khanalytic.kmm.http

import com.khanalytic.kmm.http.api.UserApi
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val httpModule = module {
    single { HttpClientFactory().create() }
    single { UserApi() }
}