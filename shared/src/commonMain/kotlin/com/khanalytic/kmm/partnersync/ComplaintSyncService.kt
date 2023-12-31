package com.khanalytic.kmm.partnersync

import com.khanalytic.integrations.PlatformApi
import com.khanalytic.kmm.http.api.ComplaintApi
import com.khanalytic.kmm.http.requests.UpdateComplaintsRequest
import com.khanalytic.kmm.http.requests.UserApiRequest
import com.khanalytic.kmm.http.requests.toUserAuthRequest
import com.khanalytic.models.User
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ComplaintSyncService: KoinComponent {
    private val complaintApi: ComplaintApi by inject()

    @Throws(Exception::class)
    suspend fun syncComplaints(
        user: User,
        platformApi: PlatformApi,
        platformBrandId: Long,
        remoteBrandId: String,
        datesToSync: List<LocalDate>
    ) = coroutineScope {
        val complaints = datesToSync.map { date ->
            async { platformApi.getComplaints(platformBrandId, remoteBrandId, date, date) }
        }.awaitAll().flatten()
        val request = UserApiRequest(
            UpdateComplaintsRequest(complaints, datesToSync, platformBrandId),
            user.toUserAuthRequest()
        )
        complaintApi.update(request)
    }
}