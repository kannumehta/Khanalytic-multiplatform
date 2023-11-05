package com.khanalytic.kmm.http

import com.khanalytic.database.shared.UserPlatformCookieDao
import com.khanalytic.models.UserPlatformCookie
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import io.ktor.util.date.GMTDate
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import com.khanalytic.models.Cookie as ModelCookie


class HttpUserPlatformCookieStorage(
    private val userPlatformCookieDao: UserPlatformCookieDao,
    private val userId: Long,
    private val platformId: Long,
    private val userPlatformCookieId: Long? = null,
) : CookiesStorage {

    private var id: Long? = userPlatformCookieId
    private val mutex = Mutex()

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        mutex.withLock {
            val record = if (userPlatformCookieId != null) {
                val userPlatformCookie = userPlatformCookieDao.getById(userPlatformCookieId)
                userPlatformCookie.copy(
                    cookies = userPlatformCookie.cookies.plus(cookie.toModelCookie())
                )
            } else {
                UserPlatformCookie(
                    id = 0,
                    userId = userId,
                    platformId = platformId,
                    cookies = listOf(cookie.toModelCookie())
                )
            }
            id = userPlatformCookieDao.upsert(record).id
        }
    }

    override suspend fun get(requestUrl: Url): List<Cookie> =
        mutex.withLock {
            if (id != null) {
                userPlatformCookieDao.getById(id!!).cookies.map { it.toHttpCookie() }
            } else { listOf() }
        }

    override fun close() {
        TODO("Not yet implemented")
    }

    companion object {
        private fun Cookie.toModelCookie(): ModelCookie =
            ModelCookie(
                name = name,
                value = value,
                domain = domain,
                path = path,
                expires = expires?.timestamp,
                isSecure = secure,
                isHttpOnly = httpOnly,
                maxAge = maxAge.toLong()
            )

        private fun ModelCookie.toHttpCookie(): Cookie =
            Cookie(
                name = name,
                value = value,
                domain = domain,
                path = path,
                expires = expires?.let { GMTDate(it) },
                secure = isSecure ?: false,
                httpOnly = isHttpOnly ?: false,
                maxAge = maxAge?.toInt() ?: 0
            )
    }
}