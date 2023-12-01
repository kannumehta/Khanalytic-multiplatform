package com.khanalytic.kmm.ui.screens.businessreport

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import com.khanalytic.kmm.darkGreenColor
import com.khanalytic.kmm.http.api.BusinessReportApi
import com.khanalytic.kmm.http.responses.AnalyticsReport
import com.khanalytic.kmm.listDividerColor
import com.khanalytic.kmm.primaryColor
import com.khanalytic.kmm.ui.common.DefaultHeading
import com.khanalytic.kmm.ui.common.DefaultSpacer
import com.khanalytic.kmm.ui.common.DefaultText
import com.khanalytic.kmm.ui.common.ListDivider
import com.khanalytic.kmm.ui.common.MathUtils
import com.khanalytic.kmm.ui.common.MathUtils.toPercentageString
import com.khanalytic.kmm.ui.common.PieChart
import com.khanalytic.kmm.ui.common.PieChartEntry
import com.khanalytic.kmm.ui.common.SingleLineSubtitleText
import com.khanalytic.kmm.ui.common.collectAsStateMultiplatform
import com.khanalytic.kmm.ui.screens.anaytics.AnalyticsPagerScreen
import com.khanalytic.kmm.ui.screens.anaytics.AnalyticsPagerScreenModel
import com.khanalytic.kmm.ui.screens.anaytics.ScreenType
import com.khanalytic.models.Platform
import com.khanalytic.models.SaleReport
import com.khanalytic.models.combined
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BusinessReportScreen : AnalyticsPagerScreen<List<SaleReport>>(), KoinComponent {
    @Composable
    override fun DrawItem(data: List<AnalyticsReport<List<SaleReport>>>, selectedIndex: Int) {
        val deliveryType = getScreenModel().deliveryTypeFlow.collectAsStateMultiplatform().value
        val reports = data[selectedIndex].report
        val deliveredReports = reports.filter { it.isDelivered }
        val cancelledReports = reports.filterNot { it.isDelivered }

        val selectedReports = when(deliveryType) {
            DeliveryType.All -> reports
            DeliveryType.Delivered -> deliveredReports
            DeliveryType.Cancelled -> cancelledReports
        }
        val combinedReports = selectedReports.groupBy { it.platformId }.values.map {
            it.combined()!!.copy(platformId = it.first().platformId)
        }
        val allReports =
            if (combinedReports.size > 1) {
                listOf(combinedReports.combined()!!.copy(platformId = 0)).plus(combinedReports)
            } else { combinedReports }

        val previousReports = if (selectedIndex + 1 < data.size) {
            data[selectedIndex + 1].report
        } else listOf()
        val previousDeliveredReports = reports.filter { it.isDelivered }
        val previousCancelledReports = reports.filterNot { it.isDelivered }
        val previousSelectedReports = when(deliveryType) {
            DeliveryType.All -> previousReports
            DeliveryType.Delivered -> previousDeliveredReports
            DeliveryType.Cancelled -> previousCancelledReports
        }
        val previousCombinedReports = previousSelectedReports.groupBy { it.platformId }.values.map {
            it.combined()!!.copy(platformId = it.first().platformId)
        }

        val allPreviousReports =
            if (previousCombinedReports.size > 1) {
                listOf(previousCombinedReports.combined()!!.copy(platformId = 0)).plus(previousCombinedReports)
            } else { previousCombinedReports }


        val platformsIdMap =
            getScreenModel().platformsFlow.collectAsStateMultiplatform().value.associateBy {
                it.id
            }

        Column(modifier = Modifier.fillMaxWidth()) {
            DrawDeliveryTypeSelection()
            LazyColumn (modifier = Modifier.fillMaxSize()) {
                items(items = allReports, itemContent = {
                    val previousReport = allPreviousReports.find { saleReport ->
                        saleReport.platformId == it.platformId
                    }
                    DrawReport(it, previousReport, platformsIdMap)
                })
            }
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
    private fun getScreenModel(): BusinessReportScreenModel<List<SaleReport>> {
        val api: BusinessReportApi by inject()
        val screenType = ScreenType.BusinessReport(api)
        return rememberScreenModel { BusinessReportScreenModel(screenType) }
    }

    @Composable
    private fun DrawDeliveryTypeSelection() {
        val model = getScreenModel()
        val selectedDeliveryType = model.deliveryTypeFlow.collectAsStateMultiplatform().value
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DeliveryType.values().forEach { deliveryType ->
                val textColor =
                    if (deliveryType == selectedDeliveryType) {
                        listDividerColor()
                    } else { primaryColor() }
                val backgroundColor =
                    if (deliveryType == selectedDeliveryType) {
                        primaryColor()
                    } else { Color(0x00000000) }
                    OutlinedButton(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
                    border = BorderStroke(1.dp, primaryColor()),
                    onClick = {
                        model.setDeliveryType(deliveryType)
                    },
                ) {
                    Text(deliveryType.toString(), color = textColor)
                }
            }
        }
    }

    @Composable
    private fun DrawReport(
        report: SaleReport,
        previousReport: SaleReport?,
        platformIdMap: Map<Long, Platform>
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val platformName = platformIdMap[report.platformId]?.name ?: "Unknown Platform"
            DefaultHeading(platformName,
                modifier = Modifier.fillMaxWidth().background(listDividerColor())
                    .padding(vertical = 12.dp)
            )
            DefaultSpacer()
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                DrawPieChart(report)
            }
            DefaultSpacer()
            DrawMetrics(report, previousReport)
        }
    }

    @Composable
    private fun DrawPieChart(saleReport: SaleReport) {
        val entries = listOf(
            PieChartEntry(data = saleReport.netPayout,
                partName = SaleItemType.NetPayout().metricName,
                color = Color(0xFF2ecc71)),
            PieChartEntry(data = saleReport.tcsTds(),
                partName = SaleItemType.TcsTds().metricName,
                color = Color(0xFFf1c40f)),
            PieChartEntry(data = saleReport.totalPlatformFee(),
                partName = SaleItemType.TotalPlatformFee().metricName,
                color = Color(0xFFe74c3c)),
            PieChartEntry(data = saleReport.orderAdjustmentsForPieChart(),
                partName = SaleItemType.OrderAdjustments().metricName,
                color = Color(0xFF3498db)),
            PieChartEntry(data = saleReport.totalDiscount(),
                partName = SaleItemType.Discounts().metricName,
                color = Color(0xFFc12551)),
            PieChartEntry(data = saleReport.totalDeductions(),
                partName = SaleItemType.AdditionalDeductions().metricName,
                color = Color(0xFFff6600))
        ).filter { it.data > 0.0 }

        PieChart(entries)
    }

    @Composable
    private fun DrawMetrics(saleReport: SaleReport, previousReport: SaleReport?) {
        val model = getScreenModel()
        val expandedMap = model.itemExpandedMapState.collectAsStateMultiplatform().value
        SaleItemType.saleItemsGroup.forEach { pair ->
            val isExpanded = expandedMap[pair.first.itemType] == true
            val subItems = pair.second.mapNotNull {
                if (it.itemType.value(saleReport) != 0) {
                    it
                } else { null }
            }.plus(pair.first.itemType.createCustomItems(saleReport))
            Row(
                modifier = Modifier.fillMaxWidth().clickable {
                    model.setExpanded(pair.first.itemType, !isExpanded)
                }.padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DefaultText(pair.first.itemType.metricName)
                DefaultSpacer()
                previousReport?.let {
                    val percentage = MathUtils.percentage(
                        pair.first.itemType.value(saleReport),
                        pair.first.itemType.value(it)
                    )
                    val diffColor = if (percentage > 0) {
                        darkGreenColor()
                    } else Color.Red
                    val diffString = if (percentage > 0) {
                        "↑${percentage.toPercentageString()}"
                    } else if (percentage < 0) {
                        "↓${percentage.toPercentageString()}"
                    } else ""
                    Text(diffString, color = diffColor, fontSize = 12.sp,
                        modifier = Modifier.weight(1f))
                } ?: Text("", modifier = Modifier.weight(1f))
                DefaultText(pair.first.valueLabel(saleReport))
                if (subItems.isNotEmpty()) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Outlined.KeyboardArrowDown,
                        contentDescription = null,
                        tint = primaryColor()
                    )
                } else {
                    Row(modifier = Modifier.size(24.dp)) {  }
                }
            }

            if (isExpanded) {
                subItems.forEach { subItem ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SingleLineSubtitleText(
                            subItem.itemType.metricName
                        )
                        DefaultSpacer()
                        previousReport?.let {
                            val percentage = MathUtils.percentage(
                                subItem.itemType.value(saleReport),
                                subItem.itemType.value(it)
                            )
                            val diffColor = if (percentage > 0) {
                                darkGreenColor()
                            } else Color.Red
                            val diffString = if (percentage > 0) {
                                "↑${percentage.toPercentageString()}"
                            } else if (percentage < 0) {
                                "↓${percentage.toPercentageString()}"
                            } else ""
                            Text(diffString, color = diffColor, fontSize = 12.sp,
                                modifier = Modifier.weight(1f))
                        }
                        SingleLineSubtitleText(subItem.valueLabel(saleReport))
                        Row(modifier = Modifier.size(24.dp)) {  }
                    }
                }
            }

            ListDivider()
        }
    }
}