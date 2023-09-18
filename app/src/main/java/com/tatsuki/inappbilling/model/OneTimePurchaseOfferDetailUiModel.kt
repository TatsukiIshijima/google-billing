package com.tatsuki.inappbilling.model

import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.ProductDetails.OneTimePurchaseOfferDetails

data class OneTimePurchaseOfferDetailUiModel(
  val index: Int,
  val productId: String,
  val productType: String,
  val name: String,
  val pricingPhase: PricingPhaseUiModel
) {

  companion object {
    fun from(
      index: Int,
      productDetails: ProductDetails,
      oneTimePurchaseOfferDetails: OneTimePurchaseOfferDetails
    ): OneTimePurchaseOfferDetailUiModel {
      return OneTimePurchaseOfferDetailUiModel(
        index = index,
        productId = productDetails.productId,
        productType = productDetails.productType,
        name = productDetails.name,
        pricingPhase = PricingPhaseUiModel(oneTimePurchaseOfferDetails.formattedPrice)
      )
    }

    fun fake(
      index: Int = 0,
      productId: String = "ProductId",
      productType: String = "ProductType",
      name: String = "name",
      pricingPhase: PricingPhaseUiModel = PricingPhaseUiModel.fake(),
    ): OneTimePurchaseOfferDetailUiModel = OneTimePurchaseOfferDetailUiModel(
      index = index,
      productId = productId,
      productType = productType,
      name = name,
      pricingPhase = pricingPhase
    )
  }
}