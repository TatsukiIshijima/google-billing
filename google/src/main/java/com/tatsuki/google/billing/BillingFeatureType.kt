package com.tatsuki.google.billing

import com.android.billingclient.api.BillingClient

sealed interface BillingFeatureType {
  val value: String

  data class Subscriptions(
    override val value: String = BillingClient.FeatureType.SUBSCRIPTIONS
  ) : BillingFeatureType

  data class SubscriptionsUpdate(
    override val value: String = BillingClient.FeatureType.SUBSCRIPTIONS_UPDATE
  ) : BillingFeatureType

  data class PriceChangeConfirmation(
    override val value: String = BillingClient.FeatureType.IN_APP_MESSAGING
  ) : BillingFeatureType

  data class InAppMessaging(
    override val value: String = BillingClient.FeatureType.IN_APP_MESSAGING
  ) : BillingFeatureType

  data class ProductDetails(
    override val value: String = BillingClient.FeatureType.PRODUCT_DETAILS
  ) : BillingFeatureType
}