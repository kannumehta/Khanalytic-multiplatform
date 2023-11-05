package com.khanalytic.kmm.ui.screens.platformaccounts

import org.koin.dsl.module

val addPlatformAccountModule = module {
    factory { AddPlatformAccountScreenModel() }
}