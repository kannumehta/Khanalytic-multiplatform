package com.khanalytic.models

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object InstantUtils {
    fun Instant.toLocalDateTimeUtc() = this.toLocalDateTime(TimeZone.UTC)
}