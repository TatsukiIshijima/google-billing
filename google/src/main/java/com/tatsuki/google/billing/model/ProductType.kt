package com.tatsuki.google.billing.model

import com.android.billingclient.api.BillingClient

sealed interface ProductType {
  val value: String

  data class InApp(
    override val value: String = BillingClient.ProductType.INAPP
  ) : ProductType

  data class Subscription(
    override val value: String = BillingClient.ProductType.SUBS
  ) : ProductType
}