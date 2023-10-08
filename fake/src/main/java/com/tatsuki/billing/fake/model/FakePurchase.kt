package com.tatsuki.billing.fake.model

import com.android.billingclient.api.Purchase
import com.android.billingclient.api.Purchase.PurchaseState
import org.json.JSONObject
import java.util.UUID

data class FakePurchase(
  val products: List<String>,
  val purchaseToken: String,
  val signature: String,
  @PurchaseState val purchaseState: Int,
  val isAcknowledged: Boolean,
) {

  fun toReal(): Purchase {
    val jsonPurchaseInfo = JSONObject().apply {
      put("productId", products.first())
      put("purchaseToken", purchaseToken)
      put("purchaseState", purchaseState)
      put("acknowledged", isAcknowledged)
    }.toString()
    return Purchase(jsonPurchaseInfo, signature)
  }

  companion object {
    fun create(
      products: List<String> = listOf("productId"),
      purchaseToken: String = UUID.randomUUID().toString(),
      signature: String = "signature",
      @PurchaseState purchaseState: Int = PurchaseState.PURCHASED,
      isAcknowledged: Boolean = false,
    ): FakePurchase {
      return FakePurchase(
        products = products,
        purchaseToken = purchaseToken,
        signature = signature,
        purchaseState = purchaseState,
        isAcknowledged = isAcknowledged,
      )
    }
  }
}
