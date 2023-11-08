package com.khanalytic.kmm.geocoding

import android.content.Context
import android.location.Geocoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class PlacesApi actual constructor(): KoinComponent {
    private val context : Context by inject()
    actual suspend fun gecode(address: String): Location = withContext(Dispatchers.IO){
        val geocoder = Geocoder(context)
        val addresses = geocoder.getFromLocationName(address, 1)
        if (!addresses.isNullOrEmpty()) {
            return@withContext Location(addresses[0].latitude, addresses[0].longitude)
        } else {
            throw Exception("Could not geocode: $address")
        }
    }
}