package com.khanalytic.kmm.ui.screens.filter

import com.khanalytic.models.Brand
import com.khanalytic.models.Platform
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent


class AnalyticsFilterManager: KoinComponent {
    private val filterFlow = MutableStateFlow(AnalyticsFilter())

    fun filterFlow(): StateFlow<AnalyticsFilter> = filterFlow

    fun setReportType(reportType: ReportType) {
        filterFlow.value = filterFlow.value.copy(reportType = reportType)
    }

    fun addBrandId(brandId: Long) {
        filterFlow.value = filterFlow.value.copy(brandIds = filterFlow.value.brandIds.plus(brandId))
    }

    fun removeBrandId(brandId: Long) {
        filterFlow.value =
            filterFlow.value.copy(brandIds = filterFlow.value.brandIds.minus(brandId))
    }

    fun addPlatformId(platformId: Long) {
        filterFlow.value =
            filterFlow.value.copy(platformIds = filterFlow.value.platformIds.plus(platformId))
    }

    fun removePlatformId(platformId: Long) {
        filterFlow.value =
            filterFlow.value.copy(platformIds = filterFlow.value.platformIds.minus(platformId))
    }
}

data class AnalyticsFilter(
    val reportType: ReportType = ReportType.Daily,
    val brandIds: Set<Long> = setOf(),
    val platformIds: Set<Long> = setOf()
)

enum class ReportType {
    Daily,
    Weekly,
    Monthly,
    Yearly
}