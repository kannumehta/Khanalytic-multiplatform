package com.khanalytic.integrations.swiggy

class DataParseException(
    message: String,
    throwable: Throwable? = null
) : Exception(message, throwable)