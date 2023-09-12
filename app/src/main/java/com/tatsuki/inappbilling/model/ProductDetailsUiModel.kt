package com.tatsuki.inappbilling.model

import com.android.billingclient.api.ProductDetails

data class ProductDetailsUiModel(
  val productId: String,
  val productType: String,
  val name: String,
  val subscriptionOfferDetailsList: List<SubscriptionOfferDetailUiModel>,
) {

  companion object {
    fun from(productDetails: ProductDetails): ProductDetailsUiModel {
      return ProductDetailsUiModel(
        name = productDetails.name,
        productId = productDetails.productId,
        productType = productDetails.productType,
        subscriptionOfferDetailsList = productDetails.subscriptionOfferDetails?.map {
          SubscriptionOfferDetailUiModel.from(it)
        } ?: emptyList()
      )
    }
  }
}
