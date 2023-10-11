package com.tatsuki.billing.fake.model

import com.android.billingclient.api.Purchase
import com.android.billingclient.api.Purchase.PurchaseState
import org.json.JSONObject
import java.util.UUID

data class FakePurchase(
  val products: List<String>,
  val quantity: Int,
  val purchaseTime: Long,
  val developerPayload: String,
  val purchaseToken: String,
  val signature: String,
  @PurchaseState val purchaseState: Int,
  val isAcknowledged: Boolean,
  val isAutoRenewing: Boolean,
) {

  fun toReal(): Purchase {
    val jsonPurchaseInfo = JSONObject().apply {
      put("productId", products.first())
      put("quantity", quantity)
      put("purchaseTime", purchaseTime)
      put("developerPayload", developerPayload)
      put("purchaseToken", purchaseToken)
      put("purchaseState", purchaseState)
      put("acknowledged", isAcknowledged)
      put("autoRenewing", isAutoRenewing)
    }.toString()
    return Purchase(jsonPurchaseInfo, signature)
  }

  companion object {
    fun create(
      products: List<String> = listOf("productId"),
      quantity: Int = 1,
      purchaseTime: Long = 1695038424198,
      developerPayload: String = "developerPayload",
      purchaseToken: String = UUID.randomUUID().toString(),
      signature: String = "signature",
      @PurchaseState purchaseState: Int = PurchaseState.PURCHASED,
      isAcknowledged: Boolean = false,
      isAutoRenewing: Boolean = true,
    ): FakePurchase {
      return FakePurchase(
        products = products,
        quantity = quantity,
        purchaseTime = purchaseTime,
        developerPayload = developerPayload,
        purchaseToken = purchaseToken,
        signature = signature,
        purchaseState = purchaseState,
        isAcknowledged = isAcknowledged,
        isAutoRenewing = isAutoRenewing,
      )
    }
  }
}

fun FakePurchase.toFakePurchaseHistoryRecord(): FakePurchaseHistoryRecord {
  return FakePurchaseHistoryRecord(
    products = products,
    quantity = quantity,
    purchaseTime = purchaseTime,
    developerPayload = developerPayload,
    purchaseToken = purchaseToken,
    signature = signature,
  )
}
