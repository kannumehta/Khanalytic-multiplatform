package com.khanalytic.kmm.ui.screens.platformaccounts

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.khanalytic.database.shared.UserPlatformCookieDao
import com.khanalytic.models.UserPlatformCookie
import com.khanalytic.models.Cookie as ModelCookie
import com.multiplatform.webview.cookie.Cookie
import com.multiplatform.webview.cookie.CookieManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PlatformLoginScreenModel: ScreenModel, KoinComponent {
    private val userPlatformCookieDao by inject<UserPlatformCookieDao>()
    val userPlatformCookieFlow = MutableStateFlow<UserPlatformCookie?>(null)

    fun saveCookies(
        userId: Long,
        platformId: Long,
        cookieManager: CookieManager,
        url: String,
        userPlatformCookieId: Long? = null
    ) {
        screenModelScope.launch {
            val cookies = cookieManager.getCookies(url).map { it.toModelCookie() }
            cookieManager.removeCookies(url)
            userPlatformCookieFlow.value =
                upsertCookies(userId, platformId, cookies, userPlatformCookieId)
        }
    }

    private suspend fun upsertCookies(
        userId: Long,
        platformId: Long,
        cookies: List<ModelCookie>,
        userPlatformCookieId: Long? = null
    ): UserPlatformCookie {
        val record = UserPlatformCookie(
            id = userPlatformCookieId ?: 0L,
            userId = userId,
            platformId = platformId,
            cookies = cookies
        )
        return userPlatformCookieDao.upsert(record)
    }

    companion object {
        private fun Cookie.toModelCookie(): ModelCookie =
            ModelCookie(
                name = name,
                value = value,
                domain = domain,
                path = path,
                expires = expiresDate,
                isSecure = isSecure,
                isHttpOnly = isHttpOnly,
                maxAge = maxAge
            )

    }
}