package com.tatsuki.billing.feature.model.type

import com.android.billingclient.api.BillingClient

sealed interface ProductType {
  @BillingClient.ProductType
  val value: String

  data class InApp(
    override val value: String = BillingClient.ProductType.INAPP
  ) : ProductType

  data class Subscription(
    override val value: String = BillingClient.ProductType.SUBS
  ) : ProductType

  companion object {
    fun from(@BillingClient.ProductType value: String): ProductType {
      return when (value) {
        BillingClient.ProductType.INAPP -> InApp()
        BillingClient.ProductType.SUBS -> Subscription()
        else -> throw IllegalArgumentException("Unknown value: $value")
      }
    }
  }
}

fun ProductType.Companion.fake(
  type: ProductType = ProductType.Subscription()
): ProductType {
  return type
}