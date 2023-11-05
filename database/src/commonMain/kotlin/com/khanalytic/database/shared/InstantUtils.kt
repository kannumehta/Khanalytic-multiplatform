package com.khanalytic.database.shared

import com.khanalytic.models.InstantUtils.toLocalDateTimeUtc
import io.github.aakira.napier.Napier
import kotlinx.datetime.Instant

object InstantUtils {

    fun Instant.toDouble(): Double =
        "${toEpochMilliseconds()}.${nanosecondsOfSecond}".toDouble()

    fun Double.toInstant(): Instant {
        Napier.v("sync double time: $this")
        val millis = toLong()
        Napier.v("sync modulus: ${(this % 1)}")
        val nanos = (this % 1).toString().split(".")[1].toInt().convertToNineDigits()
        Napier.v("sync millis: $millis, nanos: $nanos")
        val instant = Instant.fromEpochSeconds(millis, nanos)
        Napier.v("sync converted date time: ${instant.toLocalDateTimeUtc().toString()}")
        return instant
    }

    private fun Int.convertToNineDigits(): Int {
        val numberAsString = toString()
        val paddingLength = 9 - numberAsString.length
        val padding = "0".repeat(paddingLength)
        return (numberAsString + padding).toInt()
    }
}