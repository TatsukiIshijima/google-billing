package com.tatsuki.billing.feature.pattern

import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesResult

sealed interface QueryPurchasesPattern {
  val result: PurchasesResult

  data class Success(
    override val result: PurchasesResult = PurchasesResult(
      billingResult = BillingResult.newBuilder()
        .setResponseCode(BillingResponseCode.OK)
        .build(),
      purchasesList = listOf(
        Purchase("", "")
      )
    )
  ): QueryPurchasesPattern

  data class ServiceUnavailableError(
    override val result: PurchasesResult = PurchasesResult(
      billingResult = BillingResult.newBuilder()
        .setResponseCode(BillingResponseCode.SERVICE_UNAVAILABLE)
        .build(),
      purchasesList = listOf()
    )
  ): QueryPurchasesPattern
}