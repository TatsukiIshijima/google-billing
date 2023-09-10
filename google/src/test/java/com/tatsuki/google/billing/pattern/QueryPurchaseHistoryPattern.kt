package com.tatsuki.google.billing.pattern

import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PurchaseHistoryResult

sealed interface QueryPurchaseHistoryPattern {
  val result: PurchaseHistoryResult

  data class Success(
    override val result: PurchaseHistoryResult = PurchaseHistoryResult(
      billingResult = BillingResult.newBuilder()
        .setResponseCode(BillingResponseCode.OK)
        .build(),
      purchaseHistoryRecordList = listOf()
    )
  ) : QueryPurchaseHistoryPattern

  data class Failure(
    override val result: PurchaseHistoryResult = PurchaseHistoryResult(
      billingResult = BillingResult.newBuilder()
        .setResponseCode(BillingResponseCode.ERROR)
        .build(),
      purchaseHistoryRecordList = null
    )
  ) : QueryPurchaseHistoryPattern
}