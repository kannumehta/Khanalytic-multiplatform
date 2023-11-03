package com.khanalytic.kmm

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val contextModule = module {
    single { androidContext() }
}