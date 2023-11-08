package com.khanalytic.kmm.geocoding

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.copy
import kotlinx.cinterop.interpretCPointer
import kotlinx.cinterop.memScoped
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

actual class PlacesApi actual constructor(): KoinComponent {
    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun gecode(address: String): Location {
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