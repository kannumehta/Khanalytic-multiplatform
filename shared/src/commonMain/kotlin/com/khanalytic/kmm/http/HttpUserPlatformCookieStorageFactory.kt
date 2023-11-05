package com.khanalytic.kmm.http

import com.khanalytic.database.shared.UserPlatformCookieDao
import io.ktor.client.plugins.cookies.CookiesStorage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HttpUserPlatformCookieStorageFactory: KoinComponent {
    private val userPlatformCookieDao: UserPlatformCookieDao by inject()

    fun create(
        userId: Long,
        platformId: Long,
        userPlatformCookieId: Long? = null,
    ): CookiesStorage = HttpUserPlatformCookieStorage(
        userPlatformCookieDao, userId, platformId, userPlatformCookieId
    )
}