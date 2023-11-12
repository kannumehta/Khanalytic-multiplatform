package com.khanalytic.kmm.partnersync

import com.khanalytic.integrations.PlatformApi
import com.khanalytic.kmm.http.api.MenuOrderApi
import com.khanalytic.kmm.http.requests.UpdateMenuOrdersRequest
import com.khanalytic.kmm.http.requests.UserApiRequest
import com.khanalytic.kmm.http.requests.toUserAuthRequest
import com.khanalytic.models.Menu
import com.khanalytic.models.User
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MenuOrderSyncService: KoinComponent {
    private val menuOrderApi: MenuOrderApi by inject()

    @Throws(Exception::class)
    suspend fun syncOrders(
        user: User,
        menu: Menu,
        platformApi: PlatformApi,
        platformBrandId: Long,
        remoteBrandId: String,
        datesToSync: List<LocalDate>
    ) {
        datesToSync.forEach { date ->
            val orders = platformApi.getOrders(platformBrandId, remoteBrandId, date, date, menu)
            val request = UserApiRequest(
                UpdateMenuOrdersRequest(orders, date),
                user.toUserAuthRequest()
            )
            menuOrderApi.update(request)
        }
    }
}