package com.tatsuki.inappbilling.model

import com.android.billingclient.api.ProductDetails.OneTimePurchaseOfferDetails

data class OneTimePurchaseOfferDetailUiModel(
  val pricingPhase: PricingPhaseUiModel
) {

  companion object {
    fun from(
      oneTimePurchaseOfferDetails: OneTimePurchaseOfferDetails
    ): OneTimePurchaseOfferDetailUiModel {
      return OneTimePurchaseOfferDetailUiModel(
        pricingPhase = PricingPhaseUiModel(oneTimePurchaseOfferDetails.formattedPrice)
      )
    }
  }
}