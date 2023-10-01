package com.tatsuki.google.billing.fake.model

import com.android.billingclient.api.ProductDetails.SubscriptionOfferDetails
import org.json.JSONArray
import org.json.JSONObject

data class FakeSubscriptionOfferDetails(
  val basePlanId: String,
  val offerId: String,
  val offerIdToken: String,
  val pricingPhases: FakePricingPhases,
  val offerTags: List<String>,
) {

  fun toJsonObj(): JSONObject {
    return JSONObject().apply {
      put("basePlanId", basePlanId)
      put("offerId", offerId)
      put("offerIdToken", offerIdToken)
      put("pricingPhases", pricingPhases.toJsonArray())
      put("offerTags", JSONArray().apply { offerTags.forEach { put(it) } })
    }
  }

  fun toReal(): SubscriptionOfferDetails {
    val constructor =
      SubscriptionOfferDetails::class.java.getDeclaredConstructor(JSONObject::class.java)
    constructor.isAccessible = true
    return constructor.newInstance(toJsonObj())
  }

  companion object {
    fun create(
      basePlanId: String = "basePlanId",
      offerId: String = "offerId",
      offerIdToken: String = "offerIdToken",
      pricingPhases: FakePricingPhases = FakePricingPhases.create(),
      offerTags: List<String> = listOf("offerTag1", "offerTag2"),
    ): FakeSubscriptionOfferDetails {
      return FakeSubscriptionOfferDetails(
        basePlanId = basePlanId,
        offerId = offerId,
        offerIdToken = offerIdToken,
        pricingPhases = pricingPhases,
        offerTags = offerTags,
      )
    }
  }
}
