package com.tatsuki.billing.fake.model

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ProductDetails
import org.json.JSONArray
import org.json.JSONObject

data class FakeProductDetails(
  val productId: String,
  @BillingClient.ProductType val type: String,
  val title: String,
  val name: String,
  val description: String,
  val subscriptionOfferDetails: List<FakeSubscriptionOfferDetails>?,
  val oneTimePurchaseOfferDetails: FakeOneTimePurchaseOfferDetails?,
) {

  fun toJsonObj(): JSONObject {
    return JSONObject().apply {
      put("productId", productId)
      put("type", type)
      put("title", title)
      put("name", name)
      put("description", description)
      if (subscriptionOfferDetails != null) {
        val subscriptionOfferDetailsJsonArray = JSONArray().apply {
          subscriptionOfferDetails.forEach { put(it.toJsonObj()) }
        }
        put("subscriptionOfferDetails", subscriptionOfferDetailsJsonArray)
      }
      if (oneTimePurchaseOfferDetails != null) {
        put("oneTimePurchaseOfferDetails", oneTimePurchaseOfferDetails.toJsonObj())
      }
    }
  }

  fun toReal(): ProductDetails {
    val constructor = ProductDetails::class.java.getDeclaredConstructor(String::class.java)
    constructor.isAccessible = true
    return constructor.newInstance(toJsonObj().toString())
  }

  companion object {
    fun create(
      productId: String = "productId",
      @BillingClient.ProductType type: String = BillingClient.ProductType.INAPP,
      title: String = "title",
      name: String = "name",
      description: String = "description",
      subscriptionOfferDetails: List<FakeSubscriptionOfferDetails>? = listOf(
        FakeSubscriptionOfferDetails.create()
      ),
      oneTimePurchaseOfferDetails: FakeOneTimePurchaseOfferDetails? = FakeOneTimePurchaseOfferDetails.create(),
    ): FakeProductDetails {
      return FakeProductDetails(
        productId = productId,
        type = type,
        title = title,
        name = name,
        description = description,
        subscriptionOfferDetails = subscriptionOfferDetails,
        oneTimePurchaseOfferDetails = oneTimePurchaseOfferDetails,
      )
    }
  }
}
