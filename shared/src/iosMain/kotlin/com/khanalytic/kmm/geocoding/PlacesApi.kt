package com.khanalytic.kmm.geocoding

import com.khanalytic.integrations.GeocoderApi
import com.khanalytic.models.Location
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.copy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import org.koin.core.component.KoinComponent
import platform.CoreLocation.CLGeocodeCompletionHandler
import platform.CoreLocation.CLGeocoder
import platform.CoreLocation.CLPlacemark
import platform.Foundation.NSError

actual class PlacesApi actual constructor(): KoinComponent, GeocoderApi {
    @OptIn(ExperimentalForeignApi::class)
    override suspend fun gecode(address: String): Location {
        val mutex = Mutex()
        var location: Location? = null
        mutex.lock()

        coroutineScope {
            launch(Dispatchers.IO) {
                val clGeocoder = CLGeocoder()
                val geocodeCompletionHandler = object : CLGeocodeCompletionHandler {
                    override fun invoke(placemarks: List<*>?, error: NSError?) {
                        if (placemarks != null && placemarks.isNotEmpty()) {
                            val placemark = placemarks[0] as CLPlacemark
                            val placemarkLocation = placemark.location
                            placemarkLocation?.coordinate?.copy {
                                location = Location(latitude, longitude)
                            }
                        }
                        mutex.unlock()
                    }
                }
                clGeocoder.geocodeAddressString(address, null, geocodeCompletionHandler)
            }
        }

        mutex.lock()
        mutex.unlock()
        if (location != null) {
            return location as Location
        } else {
            throw Exception("could not geocode : $address")
        }
    }
}