package com.khanalytic.kmm.partnersync

import com.khanalytic.database.shared.BrandDao
import com.khanalytic.database.shared.PlatformSyncTimestampDao
import com.khanalytic.integrations.PlatformApi
import com.khanalytic.kmm.http.api.BrandApi
import com.khanalytic.kmm.http.requests.CreateBrandsRequest
import com.khanalytic.kmm.http.requests.UserApiRequest
import com.khanalytic.kmm.http.requests.toUserAuthRequest
import com.khanalytic.models.User
import io.github.aakira.napier.Napier
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BrandsSyncService: KoinComponent {
    private val brandDao: BrandDao by inject()
    private val brandApi: BrandApi by inject()
    private val platformSyncTimestampDao: PlatformSyncTimestampDao by inject()

    @Throws(Exception::class)
    suspend fun syncBrands(
        user: User,
        platformApi: PlatformApi,
        platformId: Long,
        userPlatformCookieId: Long
    ) {
        val currentTimestamp = Clock.System.now()
        val lastSyncedTimestamp =
            platformSyncTimestampDao.getPlatformSyncTimeStamp(platformId)
        if (lastSyncedTimestamp != null
            && currentTimestamp.minus(lastSyncedTimestamp).inWholeHours < 24) {
            Napier.v("brands synced less than 24 hours ago, skipping")
            return
        }
        val existingRemoteBrandIds = brandDao.getAllRemoteBrandIds(platformId).toSet()
        val brands = platformApi.getBrands(platformId, existingRemoteBrandIds)
        val request =
            UserApiRequest(CreateBrandsRequest(platformId, brands), user.toUserAuthRequest())
        val createdBrands = brandApi.createBrands(request)
        brandDao.upsetBrands(createdBrands, userPlatformCookieId)
        platformSyncTimestampDao.setPlatformSyncTimeStamp(platformId, currentTimestamp)
    }
}