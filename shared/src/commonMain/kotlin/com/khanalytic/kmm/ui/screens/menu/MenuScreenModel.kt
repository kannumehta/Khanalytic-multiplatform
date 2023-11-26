package com.khanalytic.kmm.ui.screens.menu

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.khanalytic.database.shared.BrandDao
import com.khanalytic.database.shared.UserDao
import com.khanalytic.kmm.http.api.MenuApi
import com.khanalytic.kmm.http.requests.GetMenuRequest
import com.khanalytic.kmm.http.requests.UserApiRequest
import com.khanalytic.kmm.http.requests.toUserAuthRequest
import com.khanalytic.kmm.ui.screens.anaytics.AnalyticsPagerScreenModel
import com.khanalytic.kmm.ui.screens.anaytics.ScreenType
import com.khanalytic.kmm.ui.screens.filter.AnalyticsFilter
import com.khanalytic.kmm.ui.screens.filter.AnalyticsFilterManager
import com.khanalytic.kmm.ui.screens.filter.FilterScreenModel
import com.khanalytic.models.Brand
import com.khanalytic.models.Menu
import com.khanalytic.models.MenuItemStats
import com.khanalytic.models.Platform
import com.khanalytic.models.User
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MenuScreenModel<T>(
    screenType: ScreenType<T>
): AnalyticsPagerScreenModel<T>(screenType) {
    private val userDao: UserDao by inject()
    private val brandDao: BrandDao by inject()
    private val brandsFlow = MutableStateFlow(listOf<Brand>())
    private val selectedBrand = MutableStateFlow<Brand?>(null)
    private var existingFetchJob: Deferred<Unit>? = null

    val menuApi: MenuApi by inject()
    val listItemsFlow = MutableStateFlow<List<ListItemType>>(listOf())
    val magicMenuFlow = MutableStateFlow(false)


    init {
        screenModelScope.launch {
            val user = userDao.getFirstUser() ?: return@launch
            launch {
                analyticsFilterFlow().collect { filter ->
                    listItemsFlow.value = listOf()
                    existingFetchJob?.cancel()
                    if (filter != null) {
                        existingFetchJob = async {
                            fetchMenu(user, filter.brandIds.first(), filter.platformIds.toList())
                        }
                    }
                }
            }

            launch {
                val allBrands = brandDao.selectAllBrands()
                brandsFlow.value = allBrands
                selectedBrand.value = allBrands.firstOrNull()
            }

            initLoading()
        }
    }

    override fun skipLoadingDuringInit(): Boolean = true

    override fun analyticsFilterFlow(): Flow<AnalyticsFilter?> =
        analyticsFilterManager.filterFlow().combine(selectedBrand) { filter, brand ->
            brand?.let { filter.copy(brandIds = setOf(brand.id)) }
        }

    fun setMagicMenuChecked(checked: Boolean) {
        magicMenuFlow.value = checked
    }

    private suspend fun fetchMenu(user: User, brandId: Long, platformIds: List<Long>) {
        val request = UserApiRequest(
            GetMenuRequest(brandId, platformIds),
            user.toUserAuthRequest()
        )
        listItemsFlow.value = menuApi.get(request).toListItems()
    }

    companion object {
        suspend fun Menu.toListItems(): List<ListItemType> = withContext(Dispatchers.Default){
            val itemsIdMap = items.associateBy { it.remoteItemId }
            val list = mutableListOf<ListItemType>()
            categories.forEach { category ->
                list.add(ListItemType.Category(category))
                category.subcategories.flatMap { it.items }.forEach { item ->
                    if (itemsIdMap.containsKey(item.remoteItemId)) {
                        list.add(ListItemType.Item(itemsIdMap[item.remoteItemId]!!))
                    }
                }
            }
            list
        }
    }
}