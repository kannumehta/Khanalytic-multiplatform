package com.khanalytic.kmm.ui.screens.main

import org.koin.dsl.module

val mainScreenModule = module {
    factory { MainScreenModel() }
}