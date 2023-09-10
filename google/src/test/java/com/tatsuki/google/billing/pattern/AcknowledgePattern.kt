package com.tatsuki.google.billing.pattern

import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingResult

sealed interface AcknowledgePattern {
  val result: BillingResult

  data class Success(
    override val result: BillingResult = BillingResult.newBuilder()
      .setResponseCode(BillingResponseCode.OK)
      .build()
  ) : AcknowledgePattern

  data class Error(
    override val result: BillingResult = BillingResult.newBuilder()
      .setResponseCode(BillingResponseCode.ERROR)
      .build()
  ) : AcknowledgePattern
}