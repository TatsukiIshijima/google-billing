package com.tatsuki.inappbilling.model

import com.android.billingclient.api.ProductDetails

data class SubscriptionOfferDetailUiModel(
  val parentIndex: Int,
  val index: Int,
  val basePlanId: String,
  val offerId: String,
  val offerToken: String,
  val pricingPhase: PricingPhaseUiModel,
) {

  companion object {
    fun from(
      parentIndex: Int,
      index: Int,
      subscriptionOfferDetails: ProductDetails.SubscriptionOfferDetails,
    ): SubscriptionOfferDetailUiModel {

      // FIXME: Not use first
      val pricingPhase = PricingPhaseUiModel.from(
        subscriptionOfferDetails.pricingPhases.pricingPhaseList.first()
      )

      return SubscriptionOfferDetailUiModel(
        parentIndex = parentIndex,
        index = index,
        basePlanId = subscriptionOfferDetails.basePlanId,
        offerId = subscriptionOfferDetails.offerId ?: "-",
        offerToken = subscriptionOfferDetails.offerToken,
        pricingPhase = pricingPhase
      )
    }

    fun fake(
      parentIndex: Int = 0,
      index: Int = 0,
      basePlanId: String = "basePlanId",
      offerId: String = "offerId",
      offerToken: String = "offerToken",
      pricingPhase: PricingPhaseUiModel = PricingPhaseUiModel.fake()
    ): SubscriptionOfferDetailUiModel = SubscriptionOfferDetailUiModel(
      parentIndex = parentIndex,
      index = index,
      basePlanId = basePlanId,
      offerId = offerId,
      offerToken = offerToken,
      pricingPhase = pricingPhase,
    )
  }
}
