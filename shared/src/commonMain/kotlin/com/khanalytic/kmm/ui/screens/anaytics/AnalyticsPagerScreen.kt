package com.khanalytic.kmm.ui.screens.anaytics

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowLeft
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.khanalytic.kmm.http.responses.AnalyticsReport
import com.khanalytic.kmm.primaryColor
import com.khanalytic.kmm.subtitleTextColor
import com.khanalytic.kmm.ui.common.DefaultHeading
import com.khanalytic.kmm.ui.common.DefaultSpacer
import com.khanalytic.kmm.ui.common.DefaultText
import com.khanalytic.kmm.ui.common.collectAsStateMultiplatform
import com.khanalytic.kmm.ui.screens.filter.FilterScreen
import com.khanalytic.kmm.ui.screens.filter.FilterScreenModel
import com.khanalytic.kmm.ui.screens.filter.Utils.uiLabel
import kotlinx.coroutines.launch

abstract class AnalyticsPagerScreen<T>: FilterScreen() {

    @Composable
    abstract fun DrawItem(data: List<AnalyticsReport<T>>, selectedIndex: Int)

    @Composable
    abstract fun DrawItemWithoutData()

    abstract fun shouldDrawItemWithoutData(): Boolean

    @Composable
    abstract fun getAnalyticsScreenModel(): AnalyticsPagerScreenModel<T>

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun ScreenContent(filterScreenModel: FilterScreenModel) {
        val model = getAnalyticsScreenModel()
        val pages = model.analyticsPagesFlow.collectAsStateMultiplatform().value
        val data = pages.mapNotNull {
            if (it is AnalyticsPage.Data<*>) {
                (it as AnalyticsPage.Data<T>).analyticsReport
            } else { null }
        }
        val hasExtraLoadingPage = pages.any { it is AnalyticsPage.Loading }
        val pageCountState = remember(pages.size) { pages.size }
        val pagerState = rememberPagerState(pageCount = { pageCountState })
        val filter = model.appliedAnalyticsFilterFlow.collectAsStateMultiplatform().value
        val scope = rememberCoroutineScope()

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top
        ) { pageIndex ->
            if (filter != null && pageIndex > pages.size - 4) {
                scope.launch { model.tryLoadNextBatch(filter) }
            }

            when(pages[pageIndex]) {
                is AnalyticsPage.Loading -> LoadingIndicator()
                is AnalyticsPage.NoDataAvailable -> {
                    if (shouldDrawItemWithoutData()) {
                        DrawItemWithoutData()
                    } else {
                        DefaultText("No data available")
                    }
                }
                is AnalyticsPage.Data<*> -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        CurrentDateRow(filterScreenModel, pagerState, data, pageIndex)
                        DrawItem(data, pageIndex)
                    }
                }
            }
        }
    }

    @Composable
    private fun LoadingIndicator() {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp))
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun CurrentDateRow(
        filterScreenModel: FilterScreenModel,
        pagerState: PagerState,
        data: List<AnalyticsReport<T>>,
        selectedIndex: Int
    ) {
        val date = data[selectedIndex].date
        val reportType =
            filterScreenModel.filterFlow().collectAsStateMultiplatform().value.reportType
        val label = reportType.uiLabel(date)
        val iconSizeModifier = Modifier.size(32.dp)
        val scope = rememberCoroutineScope()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                if (selectedIndex > 0) {
                    scope.launch {
                        pagerState.scrollToPage(selectedIndex - 1)
                    }
                }
            }) {
                Icon(
                    modifier = iconSizeModifier,
                    imageVector = Icons.Outlined.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = if (selectedIndex > 0) primaryColor() else subtitleTextColor(),
                )
            }
            DefaultText(label)
            IconButton(onClick = {
                if (selectedIndex < data.size - 1) {
                    scope.launch { pagerState.scrollToPage(selectedIndex + 1) }
                }
            }) {
                Icon(
                    modifier = iconSizeModifier,
                    imageVector = Icons.Outlined.KeyboardArrowRight,
                    contentDescription = null,
                    tint = if (selectedIndex < data.size - 1) primaryColor() else subtitleTextColor()
                )
            }
        }
    }
}