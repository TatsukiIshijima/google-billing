package com.tatsuki.google.billing.pattern

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.ProductDetailsResult

sealed interface QueryProductDetailsPattern {
  val result: ProductDetailsResult

  data class Success(
    override val result: ProductDetailsResult =
      ProductDetailsResult(
        billingResult = BillingResult.newBuilder()
          .setResponseCode(BillingClient.BillingResponseCode.OK)
          .build(),
        productDetailsList = listOf()
      )
  ) : QueryProductDetailsPattern

  data class Error(
    override val result: ProductDetailsResult =
      ProductDetailsResult(
        billingResult = BillingResult.newBuilder()
          .setResponseCode(BillingClient.BillingResponseCode.ERROR)
          .build(),
        productDetailsList = null
      )
  ) : QueryProductDetailsPattern
}