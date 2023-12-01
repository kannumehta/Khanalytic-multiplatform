package com.khanalytic.kmm.ui.screens.businessreport

import cafe.adriel.voyager.core.model.screenModelScope
import com.khanalytic.database.shared.PlatformDao
import com.khanalytic.kmm.ui.screens.anaytics.AnalyticsPagerScreenModel
import com.khanalytic.kmm.ui.screens.anaytics.ScreenType
import com.khanalytic.models.Platform
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import org.koin.core.component.inject

class BusinessReportScreenModel<T> (
    screenType: ScreenType<T>
): AnalyticsPagerScreenModel<T>(screenType) {
    private val platformDao: PlatformDao by inject()

    val itemExpandedMapState = MutableStateFlow(mapOf<SaleItemType, Boolean>())
    val platformsFlow = MutableStateFlow(listOf<Platform>())
    val deliveryTypeFlow = MutableStateFlow(DeliveryType.All)

    init {
        screenModelScope.launch {
            platformDao.getAllPlatformsAsFlow().collect() {
                platformsFlow.value = listOf(
                    Platform(0, "Total", Instant.DISTANT_FUTURE, Instant.DISTANT_FUTURE)
                ).plus(it)
            }
        }
    }

    fun setExpanded(saleItemType: SaleItemType, expanded: Boolean) {
        itemExpandedMapState.value =
            itemExpandedMapState.value.plus(Pair(saleItemType, expanded))
    }

    fun setDeliveryType(deliveryType: DeliveryType) {
        deliveryTypeFlow.value = deliveryType
    }
}