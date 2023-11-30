package com.khanalytic.kmm.ui.common

import kotlin.math.roundToInt
import kotlin.math.roundToLong

object MathUtils {
    fun Float.round(decimalDigits: Int): Float {
        var multiplier = 1.0f
        repeat(decimalDigits) { multiplier *= 10.0f }
        return (this * multiplier).roundToLong() / multiplier
    }

    fun Double.round(decimalDigits: Int): Double {
        var multiplier = 1.0
        repeat(decimalDigits) { multiplier *= 10.0 }
        return (this * multiplier).roundToLong() / multiplier
    }
}