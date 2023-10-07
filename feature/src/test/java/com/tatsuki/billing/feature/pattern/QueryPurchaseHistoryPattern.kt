package com.tatsuki.billing.feature.pattern

import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PurchaseHistoryRecord
import com.android.billingclient.api.PurchaseHistoryResult

sealed interface QueryPurchaseHistoryPattern {
  val result: PurchaseHistoryResult

  data class Success(
    override val result: PurchaseHistoryResult = PurchaseHistoryResult(
      billingResult = BillingResult.newBuilder()
        .setResponseCode(BillingResponseCode.OK)
        .build(),
      purchaseHistoryRecordList = listOf(
        PurchaseHistoryRecord("", "")
      )
    )
  ) : QueryPurchaseHistoryPattern

  data class Error(
    override val result: PurchaseHistoryResult = PurchaseHistoryResult(
      billingResult = BillingResult.newBuilder()
        .setResponseCode(BillingResponseCode.ERROR)
        .build(),
      purchaseHistoryRecordList = null
    )
  ) : QueryPurchaseHistoryPattern
}