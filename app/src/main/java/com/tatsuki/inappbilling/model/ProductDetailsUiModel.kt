package com.tatsuki.inappbilling.model

import com.android.billingclient.api.ProductDetails

data class ProductDetailsUiModel(
  val productId: String,
  val productType: String,
  val name: String,
  val subscriptionOfferDetailsList: List<SubscriptionOfferDetailUiModel>,
  val oneTimePurchaseOfferDetail: OneTimePurchaseOfferDetailUiModel?,
) {

  companion object {
    fun from(productDetails: ProductDetails): ProductDetailsUiModel {
      return ProductDetailsUiModel(
        name = productDetails.name,
        productId = productDetails.productId,
        productType = productDetails.productType,
        subscriptionOfferDetailsList = productDetails.subscriptionOfferDetails?.map {
          SubscriptionOfferDetailUiModel.from(it)
        } ?: emptyList(),
        oneTimePurchaseOfferDetail = productDetails.oneTimePurchaseOfferDetails?.let {
          OneTimePurchaseOfferDetailUiModel.from(it)
        }
      )
    }

    fun fake(
      productId: String = "productId",
      productType: String = "productType",
      name: String = "name"
    ): ProductDetailsUiModel = ProductDetailsUiModel(
      productId = productId,
      productType = productType,
      name = name,
      subscriptionOfferDetailsList = (0..2).map { SubscriptionOfferDetailUiModel.fake() },
      oneTimePurchaseOfferDetail = null
    )
  }
}
