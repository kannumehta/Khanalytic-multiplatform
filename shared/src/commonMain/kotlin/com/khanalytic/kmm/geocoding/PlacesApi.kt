package com.khanalytic.kmm.geocoding

import org.koin.core.component.KoinComponent

expect class PlacesApi constructor() : KoinComponent {
    suspend fun gecode(address: String): Location
}