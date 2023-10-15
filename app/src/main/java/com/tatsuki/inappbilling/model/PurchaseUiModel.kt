package com.tatsuki.inappbilling.model

import com.android.billingclient.api.Purchase
import com.tatsuki.billing.feature.model.type.PurchaseState

data class PurchaseUiModel(
  val products: List<String>,
  val purchaseState: PurchaseState,
  val quantity: Int,
  val purchaseTime: Long,
  val orderId: String?,
  val isAcknowledged: Boolean,
) {

  companion object {
    fun from(purchase: Purchase): PurchaseUiModel {
      return PurchaseUiModel(
        products = purchase.products,
        purchaseState = PurchaseState.from(purchase.purchaseState),
        quantity = purchase.quantity,
        purchaseTime = purchase.purchaseTime,
        orderId = purchase.orderId,
        isAcknowledged = purchase.isAcknowledged,
      )
    }
  }
}

fun PurchaseUiModel.Companion.fake(
  products: List<String> = listOf("product1"),
  purchaseState: PurchaseState = PurchaseState.Purchased(),
  quantity: Int = 1,
  purchaseTime: Long = 1695038424198,
  orderId: String? = "orderId",
  isAcknowledged: Boolean = false,
): PurchaseUiModel {
  return PurchaseUiModel(
    products = products,
    purchaseState = purchaseState,
    quantity = quantity,
    purchaseTime = purchaseTime,
    orderId = orderId,
    isAcknowledged = isAcknowledged,
  )
}
