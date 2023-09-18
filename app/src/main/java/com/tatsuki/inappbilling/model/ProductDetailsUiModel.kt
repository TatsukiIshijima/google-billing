package com.tatsuki.inappbilling.model

import com.android.billingclient.api.ProductDetails

data class ProductDetailsUiModel(
  val index: Int,
  val productId: String,
  val productType: String,
  val name: String,
  val subscriptionOfferDetailsList: List<SubscriptionOfferDetailUiModel>,
  val oneTimePurchaseOfferDetail: OneTimePurchaseOfferDetailUiModel?,
) {

  companion object {
    fun from(
      index: Int,
      productDetails: ProductDetails
    ): ProductDetailsUiModel {
      return ProductDetailsUiModel(
        index = index,
        name = productDetails.name,
        productId = productDetails.productId,
        productType = productDetails.productType,
        subscriptionOfferDetailsList = productDetails.subscriptionOfferDetails?.mapIndexed { childIndex, subscriptionOfferDetails ->
          SubscriptionOfferDetailUiModel.from(
            parentIndex = index,
            index = childIndex,
            subscriptionOfferDetails
          )
        } ?: emptyList(),
        oneTimePurchaseOfferDetail =
        productDetails.oneTimePurchaseOfferDetails?.let { oneTimePurchaseOfferDetails ->
          OneTimePurchaseOfferDetailUiModel.from(
            index = index,
            productDetails = productDetails,
            oneTimePurchaseOfferDetails = oneTimePurchaseOfferDetails
          )
        }
      )
    }

    fun fake(
      index: Int = 0,
      productId: String = "productId",
      productType: String = "productType",
      name: String = "name"
    ): ProductDetailsUiModel = ProductDetailsUiModel(
      index = index,
      productId = productId,
      productType = productType,
      name = name,
      subscriptionOfferDetailsList = (0..2).map { SubscriptionOfferDetailUiModel.fake() },
      oneTimePurchaseOfferDetail = OneTimePurchaseOfferDetailUiModel.fake()
    )
  }
}
