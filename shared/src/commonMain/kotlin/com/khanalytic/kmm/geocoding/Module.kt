package com.khanalytic.kmm.geocoding

import com.khanalytic.integrations.GeocoderApi
import org.koin.dsl.module

val geocodingModule = module {
    single<GeocoderApi> { PlacesApi() }
}