package com.tatsuki.billing.fake.model

import com.android.billingclient.api.ProductDetails.PricingPhases
import org.json.JSONArray

data class FakePricingPhases(
  val list: List<FakePricingPhase>,
) {

  fun toJsonArray(): JSONArray {
    return JSONArray().apply {
      list.forEach {
        put(it.toJsonObj())
      }
    }
  }

  fun toReal(): PricingPhases {
    val constructor = PricingPhases::class.java.getDeclaredConstructor(JSONArray::class.java)
    constructor.isAccessible = true
    return constructor.newInstance(toJsonArray())
  }

  companion object {
    fun create(
      list: List<FakePricingPhase> = listOf(FakePricingPhase.create())
    ): FakePricingPhases {
      return FakePricingPhases(
        list = list
      )
    }
  }
}
