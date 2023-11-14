package com.khanalytic.kmm.ui.screens.user

import org.koin.dsl.module

val userScreenModule = module {
    factory { UserScreenModel() }
}