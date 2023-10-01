package com.tatsuki.billing.fake.model

import com.android.billingclient.api.Purchase
import org.json.JSONObject

data class FakePurchase(
  val products: List<String>,
  val purchaseToken: String,
  val signature: String,
) {

  fun toReal(): Purchase {
    val jsonPurchaseInfo = JSONObject().apply {
      put("productId", products.first())
      put("purchaseToken", purchaseToken)
    }.toString()
    return Purchase(jsonPurchaseInfo, signature)
  }

  companion object {
    fun create(
      products: List<String> = listOf("productId"),
      purchaseToken: String = "purchaseToken",
      signature: String = "signature",
    ): FakePurchase {
      return FakePurchase(
        products = products,
        purchaseToken = purchaseToken,
        signature = signature,
      )
    }
  }
}
