package com.khanalytic.kmm.importing.intgerations.swiggy

import com.khanalytic.kmm.importing.intgerations.PlatformResponseParser
import com.khanalytic.kmm.importing.intgerations.swiggy.responses.BrandDetail
import com.khanalytic.kmm.importing.intgerations.swiggy.responses.BrandDetailResponse
import com.khanalytic.kmm.importing.intgerations.swiggy.responses.BrandsResponse
import kotlinx.serialization.json.Json

class SwiggyResponseParser(private val serializer: Json) : PlatformResponseParser {
    override fun parseBrandIds(response: String): List<String> {
        val regex = Regex(".*globals.userInfoDataParam\\s*=\\s*(.*?);.*")
        return regex.find(response)?.groups?.get(1)?.value?.let { json: String ->
            serializer.decodeFromString<BrandsResponse>(json).outlets.map { it.resId.toString() }
        } ?: throw DataParseException("failed to extract brands list")
    }

    @Throws(Exception::class) override fun parseBrand(response: String): BrandDetail =
        serializer.decodeFromString<BrandDetailResponse>(response).data
}