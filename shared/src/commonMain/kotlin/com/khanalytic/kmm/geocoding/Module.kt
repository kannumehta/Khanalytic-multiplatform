package com.khanalytic.kmm.geocoding

import org.koin.dsl.module

val geocodingModule = module {
    single { PlacesApi() }
}