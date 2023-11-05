package com.khanalytic.kmm.http

import com.khanalytic.database.shared.UserPlatformCookieDao
import io.ktor.client.plugins.cookies.CookiesStorage
import org.koin.core.component.KoinComponent

class HttpUserPlatformCookieStorageFactory: KoinComponent {
    fun create(
        userPlatformCookieDao: UserPlatformCookieDao,
        userId: Long,
        platformId: Long,
        userPlatformCookieId: Long? = null,
    ): CookiesStorage = HttpUserPlatformCookieStorage(
        userPlatformCookieDao, userId, platformId, userPlatformCookieId
    )
}