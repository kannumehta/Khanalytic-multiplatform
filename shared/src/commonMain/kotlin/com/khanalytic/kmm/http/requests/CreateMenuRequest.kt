package com.khanalytic.kmm.http.requests

import com.khanalytic.models.Menu
import kotlinx.serialization.Serializable

@Serializable
data class CreateMenuRequest(
    val menu: Menu
)