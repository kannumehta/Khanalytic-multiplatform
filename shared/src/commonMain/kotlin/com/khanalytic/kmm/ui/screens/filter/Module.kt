package com.khanalytic.kmm.ui.screens.filter

import org.koin.dsl.module

val filterScreenModule = module {
    single { AnalyticsFilterManager() }
}