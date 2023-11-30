package com.khanalytic.kmm.ui.screens.businessreport

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.model.rememberScreenModel
import com.khanalytic.kmm.http.api.BusinessReportApi
import com.khanalytic.kmm.http.responses.AnalyticsReport
import com.khanalytic.kmm.ui.common.PieChart
import com.khanalytic.kmm.ui.common.PieChartEntry
import com.khanalytic.kmm.ui.screens.anaytics.AnalyticsPagerScreen
import com.khanalytic.kmm.ui.screens.anaytics.AnalyticsPagerScreenModel
import com.khanalytic.kmm.ui.screens.anaytics.ScreenType
import com.khanalytic.models.SaleReport
import com.khanalytic.models.combined
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BusinessReportScreen : AnalyticsPagerScreen<List<SaleReport>>(), KoinComponent {
    @Composable
    override fun DrawItem(data: List<AnalyticsReport<List<SaleReport>>>, selectedIndex: Int) {
        val reports = data[selectedIndex].report
        val deliveredOrderReports = reports.filter { it.isDelivered }
        val cancelledOrderReports = reports.filterNot { it.isDelivered }

        val combinedReports = reports.groupBy { it.platformId }.values.map {
            it.combined()!!
        }

        val allReports =
            if (combinedReports.size > 1) {
                listOf(combinedReports.combined()!!.copy(platformId = 0)).plus(combinedReports)
            } else { combinedReports }
        LazyColumn (modifier = Modifier.fillMaxSize()) {
            items(items = allReports, itemContent = { DrawReport(it) })
        }
    }

    @Composable
    override fun DrawItemWithoutData() {
        throw NotImplementedError("this should never happen")
    }

    override fun shouldDrawItemWithoutData(): Boolean = false

    @Composable
    override fun getAnalyticsScreenModel(): AnalyticsPagerScreenModel<List<SaleReport>> =
        getScreenModel()

    @Composable
    private fun getScreenModel(): AnalyticsPagerScreenModel<List<SaleReport>> {
        val api: BusinessReportApi by inject()
        val screenType = ScreenType.BusinessReport(api)
        return rememberScreenModel { AnalyticsPagerScreenModel(screenType) }
    }

    @Composable
    private fun DrawReport(report: SaleReport) {
        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            DrawPieChart(report)
        }
    }

    @Composable
    private fun DrawPieChart(saleReport: SaleReport) {
        val entries = listOf(
            PieChartEntry(data = saleReport.netPayout, partName = "Net Payout",
                color = Color(0xFF2ecc71)),
            PieChartEntry(data = saleReport.tcsTds(), partName = "TCS/TDS",
                color = Color(0xFFf1c40f)),
            PieChartEntry(data = saleReport.totalPlatformFee(), partName = "Platform Fee",
                color = Color(0xFFe74c3c)),
            PieChartEntry(data = saleReport.orderAdjustmentsForPieChart(), partName = "Adjustments",
                color = Color(0xFF3498db)),
            PieChartEntry(data = saleReport.totalDiscount(), partName = "Discounts",
                color = Color(0xFFc12551)),
            PieChartEntry(data = saleReport.totalDeductions(), partName = "Deductions",
                color = Color(0xFFff6600))
        ).filter { it.data > 0.0 }

        PieChart(entries)
    }
}