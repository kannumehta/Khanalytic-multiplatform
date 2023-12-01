package com.khanalytic.kmm.ui.common

object MathUtils {
    fun Float.round(decimalDigits: Int): Float {
        var multiplier = 1.0f
        repeat(decimalDigits) { multiplier *= 10.0f }
        return (this * multiplier).toLong() / multiplier
    }

    fun Double.round(decimalDigits: Int): Double {
        var multiplier = 1.0
        repeat(decimalDigits) { multiplier *= 10.0 }
        return (this * multiplier).toLong() / multiplier
    }

    fun percentage(numerator: Float, denominator: Float): Float =
        if (denominator != 0f) {
            ((numerator / denominator) * 100f).round(2)
        } else 0f

    fun percentage(numerator: Int, denominator: Int): Float =
        if (denominator != 0) {
            ((numerator.toFloat() / denominator) * 100f).round(2)
        } else 0f

    fun Float.toPercentageString() = "$this%"


}