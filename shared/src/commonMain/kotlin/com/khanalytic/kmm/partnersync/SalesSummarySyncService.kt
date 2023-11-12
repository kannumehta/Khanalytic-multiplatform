package com.khanalytic.kmm.partnersync

import com.khanalytic.integrations.PlatformApi
import com.khanalytic.kmm.http.api.SalesSummaryApi
import com.khanalytic.kmm.http.requests.UpdateComplaintsRequest
import com.khanalytic.kmm.http.requests.UpdateSalesSummariesRequest
import com.khanalytic.kmm.http.requests.UserApiRequest
import com.khanalytic.kmm.http.requests.toUserAuthRequest
import com.khanalytic.models.User
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SalesSummarySyncService: KoinComponent {
    private val salesSummaryApi: SalesSummaryApi by inject()

    @Throws(Exception::class)
    suspend fun syncSalesSummaries(
        user: User,
        platformApi: PlatformApi,
        platformBrandId: Long,
        remoteBrandId: String,
        datesToSync: List<LocalDate>
    ) = coroutineScope {
        val summaries = datesToSync.map { date ->
            async { platformApi.getSalesSummary(platformBrandId, remoteBrandId, date, date) }
        }.awaitAll()
        val request = UserApiRequest(
            UpdateSalesSummariesRequest(summaries, datesToSync, platformBrandId),
            user.toUserAuthRequest()
        )
        salesSummaryApi.update(request)
    }
}