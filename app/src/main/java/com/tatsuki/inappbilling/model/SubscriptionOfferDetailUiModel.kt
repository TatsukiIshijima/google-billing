package com.tatsuki.inappbilling.model

import com.android.billingclient.api.ProductDetails

data class SubscriptionOfferDetailUiModel(
  val basePlanId: String,
  val offerId: String,
  val offerToken: String,
  val pricingPhase: PricingPhaseUiModel,
) {

  companion object {
    fun from(
      subscriptionOfferDetails: ProductDetails.SubscriptionOfferDetails,
    ): SubscriptionOfferDetailUiModel {

      // FIXME: Not use first
      val pricingPhase = PricingPhaseUiModel.from(
        subscriptionOfferDetails.pricingPhases.pricingPhaseList.first()
      )

      return SubscriptionOfferDetailUiModel(
        basePlanId = subscriptionOfferDetails.basePlanId,
        offerId = subscriptionOfferDetails.offerId ?: "-",
        offerToken = subscriptionOfferDetails.offerToken,
        pricingPhase = pricingPhase
      )
    }
  }
}
