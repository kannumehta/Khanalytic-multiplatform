package com.khanalytic.kmm.geocoding

import com.khanalytic.integrations.GeocoderApi
import org.koin.core.component.KoinComponent

expect class PlacesApi constructor() : KoinComponent, GeocoderApi