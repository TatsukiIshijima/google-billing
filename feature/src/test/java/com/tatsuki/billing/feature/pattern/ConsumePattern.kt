package com.tatsuki.billing.feature.pattern

import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeResult

sealed interface ConsumePattern {
  val result: ConsumeResult

  data class Success(
    override val result: ConsumeResult = ConsumeResult(
      billingResult = BillingResult.newBuilder()
        .setResponseCode(BillingResponseCode.OK)
        .build(),
      purchaseToken = "purchaseToken"
    )
  ) : ConsumePattern

  data class Error(
    override val result: ConsumeResult = ConsumeResult(
      billingResult = BillingResult.newBuilder()
        .setResponseCode(BillingResponseCode.ERROR)
        .build(),
      purchaseToken = null
    )
  ) : ConsumePattern
}