package com.tatsuki.inappbilling.model

import com.android.billingclient.api.PurchaseHistoryRecord

data class PurchaseHistoryRecordUiModel(
  val products: List<String>,
  val quantity: Int,
  val purchaseTime: Long,
) {

  companion object {
    fun from(purchaseHistoryRecord: PurchaseHistoryRecord): PurchaseHistoryRecordUiModel {
      return PurchaseHistoryRecordUiModel(
        products = purchaseHistoryRecord.products,
        quantity = purchaseHistoryRecord.quantity,
        purchaseTime = purchaseHistoryRecord.purchaseTime,
      )
    }
  }
}

fun PurchaseHistoryRecordUiModel.Companion.fake(
  products: List<String> = listOf("product1"),
  quantity: Int = 1,
  purchaseTime: Long = 1695038424198,
): PurchaseHistoryRecordUiModel {
  return PurchaseHistoryRecordUiModel(
    products = products,
    quantity = quantity,
    purchaseTime = purchaseTime,
  )
}
