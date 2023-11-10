package com.khanalytic.integrations

import com.khanalytic.models.Location

interface GeocoderApi {
    @Throws(Exception::class) suspend fun gecode(address: String): Location
}