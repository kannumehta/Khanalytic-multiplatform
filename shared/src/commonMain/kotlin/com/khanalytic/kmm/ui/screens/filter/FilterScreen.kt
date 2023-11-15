package com.khanalytic.kmm.ui.screens.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.khanalytic.kmm.listDividerColor
import com.khanalytic.kmm.ui.common.DefaultHeading
import com.khanalytic.kmm.ui.common.DefaultSpacer
import com.khanalytic.kmm.ui.common.DefaultText
import com.khanalytic.kmm.ui.common.ListDivider
import com.khanalytic.kmm.ui.common.collectAsStateMultiplatform
import com.khanalytic.models.Brand
import com.khanalytic.models.Platform

abstract class FilterScreen: Screen {
    @Composable
    abstract fun ScreenContent(filterScreenModel: FilterScreenModel)

    @Composable
    fun setFilterScreenVisible(visible: Boolean) {
        val model = rememberScreenModel { FilterScreenModel() }
        model.setFilterScreenVisibility(visible)
    }

    @Composable
    override fun Content() {
        val model = rememberScreenModel { FilterScreenModel() }
        Column(modifier = Modifier.fillMaxSize()) {
            if (model.filterScreenVisibleFlow.collectAsStateMultiplatform().value) {
                FilterScreenContent(model)
            } else {
                ScreenContent(model)
            }
        }
    }

    @Composable
    private fun FilterScreenContent(model: FilterScreenModel) {
        val items = model.listItemFlow.collectAsStateMultiplatform().value
        LazyColumn (modifier = Modifier.fillMaxSize()) {
            items(items = items, itemContent = { item ->
                when(item) {
                    is ListItem.Header -> Header(item.name)
                    is ListItem.PlatformItem -> Platform(item.platform, model)
                    is ListItem.BrandItem -> Brand(item.brand, model)
                    is ListItem.ReportTypeItem -> ReportType(model)
                }
            })
        }
    }

    @Composable
    private fun Header(name: String) {
        DefaultHeading(
            name,
            modifier = Modifier.fillMaxWidth().background(listDividerColor())
                .padding(vertical = 12.dp)
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ReportType(model: FilterScreenModel) {
        var expanded = remember { mutableStateOf(false) }
        val types = ReportType.values()
        var selectedText =
            model.filterFlow().collectAsStateMultiplatform().value.reportType.toString()

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            DefaultText("Report Type")
            DefaultSpacer()
            ExposedDropdownMenuBox(
                expanded = expanded.value,
                onExpandedChange = {
                    expanded.value = !expanded.value
                }
            ) {
                TextField(
                    value = selectedText,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
                    },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false }
                ) {
                    types.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item.toString()) },
                            onClick = {
                                model.setReportType(item)
                                expanded.value = false
                            }
                        )
                    }
                }
            }
        }
        DefaultSpacer()
    }

    @Composable
    private fun Platform(platform: Platform, model: FilterScreenModel) {
        val checkPlatformIds = model.filterFlow().collectAsStateMultiplatform().value.platformIds
        val isChecked = checkPlatformIds.isEmpty() || checkPlatformIds.contains(platform.id)
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { model.updatePlatformSelection(!isChecked, platform.id) }
            )
            DefaultSpacer()
            DefaultText(platform.name, modifier = Modifier.fillMaxWidth())
        }
    }

    @Composable
    private fun Brand(brand: Brand, model: FilterScreenModel) {
        val checkBrandIds = model.filterFlow().collectAsStateMultiplatform().value.brandIds
        val isChecked = checkBrandIds.isEmpty() || checkBrandIds.contains(brand.id)
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { model.updateBrandSelection(!isChecked, brand.id) }
            )
            DefaultSpacer()
            DefaultText(brand.name, modifier = Modifier.fillMaxWidth())
        }
    }
}