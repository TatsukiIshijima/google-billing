package com.tatsuki.billing.fake.model

import com.android.billingclient.api.PurchaseHistoryRecord
import org.json.JSONObject

data class FakePurchaseHistoryRecord(
  val products: List<String>,
  val quantity: Int,
  val purchaseTime: Long,
  val developerPayload: String,
  val purchaseToken: String,
  val signature: String,
) {

  fun toReal(): PurchaseHistoryRecord {
    val jsonPurchaseInfo = JSONObject().apply {
      put("productId", products.first())
      put("quantity", quantity)
      put("purchaseTime", purchaseTime)
      put("developerPayload", developerPayload)
      put("purchaseToken", purchaseToken)
    }.toString()
    return PurchaseHistoryRecord(jsonPurchaseInfo, signature)
  }

  companion object {
    fun create(
      products: List<String> = listOf("productId"),
      quantity: Int = 1,
      purchaseTime: Long = 1695038424198,
      developerPayload: String = "developerPayload",
      purchaseToken: String = "purchaseToken",
      signature: String = "signature"
    ): FakePurchaseHistoryRecord {
      return FakePurchaseHistoryRecord(
        products = products,
        quantity = quantity,
        purchaseTime = purchaseTime,
        developerPayload = developerPayload,
        purchaseToken = purchaseToken,
        signature = signature,
      )
    }
  }
}
