package com.tatsuki.google.billing.fake.model

import com.android.billingclient.api.ProductDetails.OneTimePurchaseOfferDetails
import com.google.gson.Gson
import org.json.JSONObject

data class FakeOneTimePurchaseOfferDetails(
  val formattedPrice: String,
  val priceAmountMicros: Long,
  val priceCurrencyCode: String,
) {

  fun toJsonObj(): JSONObject {
    return JSONObject(Gson().toJson(this))
  }

  fun toReal(): OneTimePurchaseOfferDetails {
    val constructor = OneTimePurchaseOfferDetails::class.java.getDeclaredConstructor(JSONObject::class.java)
    constructor.isAccessible = true
    return constructor.newInstance(toJsonObj())
  }

  companion object {
    fun create(
      formattedPrice: String = "¥10",
      priceAmountMicros: Long = 10000000L,
      priceCurrencyCode: String = "JPY",
    ): FakeOneTimePurchaseOfferDetails {
      return FakeOneTimePurchaseOfferDetails(
        formattedPrice = formattedPrice,
        priceAmountMicros = priceAmountMicros,
        priceCurrencyCode = priceCurrencyCode,
      )
    }
  }
}
