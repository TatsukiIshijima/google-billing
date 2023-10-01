package com.tatsuki.billing.fake.model

import com.android.billingclient.api.ProductDetails.PricingPhase
import com.android.billingclient.api.ProductDetails.RecurrenceMode
import com.google.gson.Gson
import org.json.JSONObject

data class FakePricingPhase(
  val formattedPrice: String,
  val priceAmountMicros: Long,
  val priceCurrencyCode: String,
  val billingPeriod: String,
  val billingCycleCount: Int,
  @RecurrenceMode val recurrenceMode: Int
) {

  fun toJsonObj(): JSONObject {
    return JSONObject(Gson().toJson(this))
  }

  fun toReal(): PricingPhase {
    val constructor = PricingPhase::class.java.getDeclaredConstructor(JSONObject::class.java)
    constructor.isAccessible = true
    return constructor.newInstance(toJsonObj())
  }

  companion object {
    fun create(
      formattedPrice: String = "¥10",
      priceAmountMicros: Long = 10000000L,
      priceCurrencyCode: String = "JPY",
      billingPeriod: String = "P1M",
      billingCycleCount: Int = 1,
      @RecurrenceMode recurrenceMode: Int = RecurrenceMode.INFINITE_RECURRING
    ): FakePricingPhase {
      return FakePricingPhase(
        formattedPrice = formattedPrice,
        priceAmountMicros = priceAmountMicros,
        priceCurrencyCode = priceCurrencyCode,
        billingPeriod = billingPeriod,
        billingCycleCount = billingCycleCount,
        recurrenceMode = recurrenceMode
      )
    }
  }
}
