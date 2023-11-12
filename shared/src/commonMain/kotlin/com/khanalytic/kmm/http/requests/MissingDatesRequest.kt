package com.khanalytic.kmm.http.requests

import kotlinx.serialization.Serializable

@Serializable
data class MissingDatesRequest(val platformBrandIds: List<Long>)
