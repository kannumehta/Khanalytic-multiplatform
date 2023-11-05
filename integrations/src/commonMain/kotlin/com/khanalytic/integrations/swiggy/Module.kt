package com.khanalytic.integrations.swiggy

import org.koin.dsl.module

val swiggyModule = module {
    single { SwiggyResponseParser() }
    single { SwiggyApiFactory() }
}