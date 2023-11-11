package com.khanalytic.kmm.partnersync

import com.khanalytic.integrations.PlatformApi
import com.khanalytic.kmm.http.api.MenuApi
import com.khanalytic.kmm.http.requests.CreateMenuRequest
import com.khanalytic.kmm.http.requests.UserApiRequest
import com.khanalytic.kmm.http.requests.toUserAuthRequest
import com.khanalytic.models.Menu
import com.khanalytic.models.User
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MenuSyncService: KoinComponent {
    private val menuApi: MenuApi by inject()

    @Throws(Exception::class)
    suspend fun syncMenu(
        user: User,
        platformApi: PlatformApi,
        platformBrandId: Long,
        remoteBrandId: String
    ): Menu {
        val menu = platformApi.getMenu(platformBrandId, remoteBrandId)
        val request = UserApiRequest(CreateMenuRequest(menu), user.toUserAuthRequest())
        menuApi.update(request)
        return menu
    }
}