package com.tatsuki.inappbilling.model

import com.android.billingclient.api.ProductDetails.PricingPhase

data class PricingPhaseUiModel(
  val formattedPrice: String,
) {
  companion object {
    fun from(pricingPhase: PricingPhase): PricingPhaseUiModel {
      return PricingPhaseUiModel(
        formattedPrice = pricingPhase.formattedPrice
      )
    }

    fun fake(
      formattedPrice: String = "¥ 10"
    ): PricingPhaseUiModel = PricingPhaseUiModel(
      formattedPrice = formattedPrice,
    )
  }
}
