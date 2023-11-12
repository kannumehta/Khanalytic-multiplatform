package com.khanalytic.kmm.geocoding

import android.content.Context
import android.location.Geocoder
import com.khanalytic.integrations.GeocoderApi
import com.khanalytic.models.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class PlacesApi actual constructor(): KoinComponent, GeocoderApi {
    private val context : Context by inject()
    override suspend fun gecode(address: String): Location? = withContext(Dispatchers.IO) {
        val geocoder = Geocoder(context)
        val addresses = geocoder.getFromLocationName(address, 1)
        return@withContext if (!addresses.isNullOrEmpty()) {
            Location(addresses[0].latitude, addresses[0].longitude)
        } else { null }
    }
}