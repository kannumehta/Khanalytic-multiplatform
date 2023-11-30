package com.khanalytic.models

import kotlinx.serialization.Serializable

@Serializable
data class SaleReport(
    val platformId: Int,
    val orders: Int,
    val isDelivered: Boolean,
    val subtotal: Float,
    val packagingCharge: Float,
    val deliveryCharge: Float,
    val restaurantPromoDiscount: Float,
    val restaurantOtherDiscount: Float,
    val netBillValue: Float,
    val gstCollected: Float,
    val gstRetained: Float,
    val grossSales: Float,
    val tcs: Float,
    val tds: Float,
    val cancellationRefund: Float,
    val payout: Float,
    val deductions: List<Deduction>,
    val cashPrepayment: Float,
    val customerRefund: Float,
    val netPayout: Float,
    val platformFee: Float,
    val platformFeeTaxes: Float,
    val platformPaymentFee: Float,
    val platformDeliveryCharge: Float,
    val platformFeeDiscount: Float = 0f,
    val platformAccessCharges: Float = 0f,
    val platformCancellationCharges: Float = 0f,
    val platformCallCenterFees: Float = 0f,
    val creditNoteAdjustment: Float = 0f,
    val promoRecoveryAdjustment: Float = 0f,
    val inventoryAds: Float = 0f,
) {
    fun averageOrderValue(): Float =
        if (orders > 0) { subtotal / orders }
        else { 0f }

    fun customerPayable(): Float =
        subtotal + gstCollected + packagingCharge + deliveryCharge - totalDiscount()

    fun totalDiscount(): Float = restaurantPromoDiscount + restaurantOtherDiscount

    fun totalDeductions(): Float = deductions.map { it.totalAmount }.sum()

    fun totalPlatformFee(): Float =
        platformFee + platformFeeTaxes + platformPaymentFee + platformDeliveryCharge +
                platformFeeDiscount + platformAccessCharges + platformCallCenterFees +
                platformCancellationCharges

    fun orderAdjustments(): Float =
        cashPrepayment + customerRefund + cancellationRefund + gstRetained + inventoryAds +
                creditNoteAdjustment + promoRecoveryAdjustment

    fun orderAdjustmentsForPieChart(): Float =
        cashPrepayment + customerRefund + cancellationRefund + inventoryAds +
                creditNoteAdjustment + promoRecoveryAdjustment

    fun tcsTds(): Float = tcs + tds
}

fun List<SaleReport>.combined(): SaleReport? =
    if (isEmpty()) { null }
    else {
        this.reduce { acc, curr ->
            acc.copy(
                platformId = 0,
                orders = acc.orders + curr.orders,
                subtotal = acc.subtotal + curr.subtotal,
                packagingCharge = acc.packagingCharge + curr.packagingCharge,
                deliveryCharge = acc.deliveryCharge + curr.deliveryCharge,
                restaurantPromoDiscount = acc.restaurantPromoDiscount + curr.restaurantPromoDiscount,
                restaurantOtherDiscount = acc.restaurantOtherDiscount + curr.restaurantOtherDiscount,
                netBillValue = acc.netBillValue + curr.netBillValue,
                gstCollected = acc.gstCollected + curr.gstCollected,
                gstRetained = acc.gstRetained + curr.gstRetained,
                grossSales = acc.grossSales + curr.grossSales,
                platformFee = acc.platformFee + curr.platformFee,
                platformFeeTaxes = acc.platformFeeTaxes + curr.platformFeeTaxes,
                platformPaymentFee = acc.platformPaymentFee + curr.platformPaymentFee,
                platformDeliveryCharge = acc.platformDeliveryCharge + curr.platformDeliveryCharge,
                platformFeeDiscount = acc.platformFeeDiscount + curr.platformFeeDiscount,
                platformAccessCharges = acc.platformAccessCharges + curr.platformAccessCharges,
                platformCancellationCharges = acc.platformCancellationCharges + curr.platformCancellationCharges,
                platformCallCenterFees = acc.platformCallCenterFees + curr.platformCallCenterFees,
                creditNoteAdjustment = acc.creditNoteAdjustment + curr.creditNoteAdjustment,
                promoRecoveryAdjustment = acc.promoRecoveryAdjustment + curr.promoRecoveryAdjustment,
                inventoryAds = acc.inventoryAds + curr.inventoryAds,
                tcs = acc.tcs + curr.tcs,
                tds = acc.tds + curr.tds,
                cancellationRefund = acc.cancellationRefund + curr.cancellationRefund,
                payout = acc.payout + curr.payout,
                deductions = acc.deductions.plus(curr.deductions),
                cashPrepayment = acc.cashPrepayment + curr.cashPrepayment,
                customerRefund = acc.customerRefund + curr.customerRefund,
                netPayout = acc.netPayout + curr.netPayout
            )
        }
    }