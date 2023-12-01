package com.khanalytic.kmm.ui.screens.businessreport

import com.khanalytic.models.SaleReport

sealed class SaleItemType(val metricName: String) {
    class Orders : SaleItemType("Orders")
    class AverageOrderValue : SaleItemType("Average Order Value")
    class CustomerPayable : SaleItemType("Customer Payable")
    class ItemTotal : SaleItemType("Item Total")
    class PackagingCharges : SaleItemType("Packaging Charges")
    class DeliveryCharges : SaleItemType("Delivery Charges")
    class Discounts : SaleItemType("Discounts")
    class NetBill : SaleItemType("Net Bill Value")
    class GstCollected : SaleItemType("Taxes")
    class GrossValue : SaleItemType("Gross Value")
    class TotalPlatformFee : SaleItemType("Platform Fee")
    class PlatformServiceFee : SaleItemType("Platform Service Fee")
    class PlatformTaxes : SaleItemType("Platform Taxes")
    class PlatformPaymentFee : SaleItemType("Payment Fee")
    class PlatformDeliveryCharge : SaleItemType("Platform Delivery Charge")
    class PlatFormFeeDiscount : SaleItemType("Platform Fee Discount")
    class PlatformAccessCharges : SaleItemType("Access Fee")
    class PlatformCancellationCharges : SaleItemType("Cancellation Fee")
    class PlatformCallCenterFee : SaleItemType("Call Center Fee")
    class CreditNoteAdjustment : SaleItemType("Credit/Debit Note")
    class PromoRecoveryAdjustment : SaleItemType("Promo Recovery")
    class InventoryAds : SaleItemType("Inventory Ads")
    class TcsTds : SaleItemType("TCS/TDS")
    class GstRetained : SaleItemType("GST Deduction")
    class CancellationRefund : SaleItemType("Cancellation Refund")
    class CustomerRefund : SaleItemType("Customer Refund")
    class CashPrepayment : SaleItemType("Cash Prepayment")
    class Payout : SaleItemType("Payout")
    class OrderAdjustments : SaleItemType("Adjustments")
    class AdditionalDeductions : SaleItemType("Deductions")
    class NetPayout : SaleItemType("Net Payout")
    class Custom(metricName: String, val metricValue: Float) : SaleItemType(metricName)

    fun value(saleReport: SaleReport): Int = when (this) {
        is Orders -> saleReport.orders.toFloat()
        is AverageOrderValue -> saleReport.averageOrderValue()
        is CustomerPayable -> saleReport.customerPayable()
        is ItemTotal -> saleReport.subtotal
        is PackagingCharges -> saleReport.packagingCharge
        is DeliveryCharges -> saleReport.deliveryCharge
        is Discounts -> saleReport.totalDiscount()
        is NetBill -> saleReport.netBillValue
        is GstCollected -> saleReport.gstCollected
        is GrossValue -> saleReport.grossSales
        is TotalPlatformFee -> saleReport.totalPlatformFee()
        is PlatformServiceFee -> saleReport.platformFee
        is PlatformTaxes -> saleReport.platformFeeTaxes
        is PlatformPaymentFee -> saleReport.platformPaymentFee
        is PlatformDeliveryCharge -> saleReport.platformDeliveryCharge
        is PlatFormFeeDiscount -> saleReport.platformFeeDiscount
        is PlatformAccessCharges -> saleReport.platformAccessCharges
        is PlatformCancellationCharges -> saleReport.platformCancellationCharges
        is PlatformCallCenterFee -> saleReport.platformCallCenterFees
        is CreditNoteAdjustment -> saleReport.creditNoteAdjustment
        is PromoRecoveryAdjustment -> saleReport.promoRecoveryAdjustment
        is InventoryAds -> saleReport.inventoryAds
        is TcsTds -> saleReport.tcsTds()
        is GstRetained -> saleReport.gstRetained
        is CancellationRefund -> saleReport.cancellationRefund
        is CustomerRefund -> saleReport.customerRefund
        is CashPrepayment -> saleReport.cashPrepayment
        is Payout -> saleReport.payout
        is OrderAdjustments -> saleReport.orderAdjustments()
        is AdditionalDeductions -> saleReport.totalDeductions()
        is NetPayout -> saleReport.netPayout
        is Custom -> this.metricValue
    }.toInt()

    fun createCustomItems(saleReport: SaleReport): List<SaleMetricItem> =
        when (this) {
            is AdditionalDeductions -> saleReport.deductions.map {
                SaleMetricItem(Custom(it.deductionType, it.totalAmount))
            }

            else -> listOf()
        }

    companion object {
        val saleItemsGroup = listOf(
            Pair(SaleMetricItem(Orders(), SaleMetricOperation.NoOp), listOf()),
            Pair(SaleMetricItem(CustomerPayable()), listOf(
                SaleMetricItem(ItemTotal()),
                SaleMetricItem(PackagingCharges()),
                SaleMetricItem(DeliveryCharges()),
                SaleMetricItem(Discounts(), SaleMetricOperation.Subtract),
                SaleMetricItem(GstCollected())
            )),
            Pair(SaleMetricItem(TotalPlatformFee(), SaleMetricOperation.Subtract), listOf(
                SaleMetricItem(PlatformServiceFee()),
                SaleMetricItem(PlatformTaxes()),
                SaleMetricItem(PlatformPaymentFee()),
                SaleMetricItem(PlatformDeliveryCharge()),
                SaleMetricItem(PlatFormFeeDiscount()),
                SaleMetricItem(PlatformAccessCharges()),
                SaleMetricItem(PlatformCallCenterFee()),
                SaleMetricItem(PlatformCancellationCharges())
            )),
            Pair(SaleMetricItem(OrderAdjustments(), SaleMetricOperation.Subtract), listOf(
                SaleMetricItem(CashPrepayment()),
                SaleMetricItem(GstRetained()),
                SaleMetricItem(CustomerRefund()),
                SaleMetricItem(CancellationRefund()),
                SaleMetricItem(CreditNoteAdjustment()),
                SaleMetricItem(PromoRecoveryAdjustment()),
                SaleMetricItem(InventoryAds()),
            )),
            Pair(SaleMetricItem(AdditionalDeductions(), SaleMetricOperation.Subtract), listOf()),
            Pair(SaleMetricItem(TcsTds(), SaleMetricOperation.Subtract), listOf()),
            Pair(SaleMetricItem(NetPayout(), SaleMetricOperation.NoOp),  listOf())
        )
    }
}

data class SaleMetricItem(
    val itemType: SaleItemType,
    val operationType: SaleMetricOperation = SaleMetricOperation.Add
) {
    fun valueLabel(saleReport: SaleReport): String =
        itemType.value(saleReport).let {
            if (itemType is SaleItemType.Orders) {
                "${operationType.operatorString}$it"
            } else {
                "${operationType.operatorString}₹$it"
            }
        }
}

enum class SaleMetricOperation(val operatorString: String = "") {
    NoOp,
    Add,
    Subtract("-");
}