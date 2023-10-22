package com.khanalytic.kmm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform