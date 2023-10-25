package com.khanalytic.kmm.importing.intgerations

import com.khanalytic.kmm.importing.intgerations.swiggy.responses.BrandDetail

interface PlatformResponseParser {
    @Throws(Exception::class) fun parseBrandIds(response: String): List<String>
    @Throws(Exception::class) fun parseBrand(response: String): BrandDetail
}