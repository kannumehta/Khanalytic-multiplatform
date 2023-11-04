package com.khanalytic.kmm.ui.screens.login

import org.koin.dsl.module

val registerModule = module {
    factory { RegisterScreenModel() }
}