package com.khanalytic.integrations

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform