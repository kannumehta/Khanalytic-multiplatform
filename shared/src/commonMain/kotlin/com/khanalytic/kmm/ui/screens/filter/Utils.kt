package com.khanalytic.kmm.ui.screens.filter

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus

object Utils {
    fun ReportType.uiLabel(date: LocalDate): String = when(this) {
        ReportType.Daily -> "${date.dayOfMonth} ${date.month} ${date.year}"
        ReportType.Weekly -> {
            val dayOfWeek = date.dayOfWeek.isoDayNumber
            val startDate = date.minus(DatePeriod(days = dayOfWeek - 1))
            val endDate = date.plus(DatePeriod(days = 7 - dayOfWeek))
            val dailyReportType = ReportType.Daily
            "${dailyReportType.uiLabel(startDate)} - ${dailyReportType.uiLabel(endDate)}"
        }
        ReportType.Monthly -> "${date.month} ${date.year}"
        ReportType.Yearly -> date.year.toString()
    }
}