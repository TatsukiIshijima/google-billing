package com.tatsuki.billing.feature.model.type

import com.android.billingclient.api.BillingClient

sealed interface BillingFeatureType {
  @BillingClient.FeatureType
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

  companion object {
    fun from(@BillingClient.FeatureType value: String): BillingFeatureType {
      return when (value) {
        BillingClient.FeatureType.SUBSCRIPTIONS -> Subscriptions()
        BillingClient.FeatureType.SUBSCRIPTIONS_UPDATE -> SubscriptionsUpdate()
        BillingClient.FeatureType.PRICE_CHANGE_CONFIRMATION -> PriceChangeConfirmation()
        BillingClient.FeatureType.IN_APP_MESSAGING -> InAppMessaging()
        BillingClient.FeatureType.PRODUCT_DETAILS -> ProductDetails()
        else -> throw IllegalArgumentException("Unknown value: $value")
      }
    }
  }
}