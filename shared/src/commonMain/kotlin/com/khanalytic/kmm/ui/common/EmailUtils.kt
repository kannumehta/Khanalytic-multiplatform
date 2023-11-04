package com.khanalytic.kmm.ui.common

object EmailUtils {

    private val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")

    fun isEmailValid(email: String): Boolean = emailRegex.matches(email)
}