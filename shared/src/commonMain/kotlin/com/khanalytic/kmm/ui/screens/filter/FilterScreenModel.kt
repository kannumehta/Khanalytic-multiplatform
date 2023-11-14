package com.khanalytic.kmm.ui.screens.filter

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.khanalytic.database.shared.BrandDao
import com.khanalytic.database.shared.PlatformDao
import com.khanalytic.models.Brand
import com.khanalytic.models.Platform
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FilterScreenModel: ScreenModel, KoinComponent {
    private val brandDao: BrandDao by inject()
    private val platformDao: PlatformDao by inject()

    val brandsStateFlow = MutableStateFlow(listOf<Brand>())
    val platformsStateFlow = MutableStateFlow(listOf<Platform>())
    val listItemFlow = MutableStateFlow(listOf<ListItem>())
    val filterScreenVisibleFlow = MutableStateFlow(false)

    init {
        screenModelScope.launch {
            launch {
                brandDao.selectAllBrandsAsFlow().collect {
                    brandsStateFlow.value = it
                    updateListItems()
                }
            }

            launch {
                platformDao.getAllPlatformsAsFlow().collect {
                    platformsStateFlow.value = it
                    updateListItems()

                }
            }
        }
    }

    fun setFilterScreenVisibility(visible: Boolean) {
        filterScreenVisibleFlow.value = visible
    }

    private fun updateListItems() {
        val brands = brandsStateFlow.value
        val platforms = platformsStateFlow.value
        val listItems = mutableListOf<ListItem>()
        listItems.add(ListItem.ReportTypeItem)
        if (platforms.isNotEmpty()) {
            listItems.add(ListItem.Header("Platforms"))
            platforms.forEach { listItems.add(ListItem.PlatformItem(it, true)) }
        }
        if (brands.isNotEmpty()) {
            listItems.add(ListItem.Header("Brands"))
            brands.forEach { listItems.add(ListItem.BrandItem(it, true)) }
        }
        listItemFlow.value = listItems
    }
}