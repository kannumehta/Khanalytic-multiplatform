package com.khanalytic.kmm.ui.screens.anaytics

import com.khanalytic.kmm.http.responses.AnalyticsReport

sealed class AnalyticsPage {
    data object Loading: AnalyticsPage()
    data object NoDataAvailable: AnalyticsPage()

    data class Data<T>(val analyticsReport: AnalyticsReport<T>): AnalyticsPage()
}