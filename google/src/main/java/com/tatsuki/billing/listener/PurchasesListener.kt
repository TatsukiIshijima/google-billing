package com.tatsuki.billing.listener

import androidx.annotation.VisibleForTesting
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.tatsuki.billing.model.RequestId
import java.util.UUID

object PurchasesListener : PurchasesUpdatedListener {

  @VisibleForTesting
  var purchaseRequestId: RequestId? = null

  @VisibleForTesting
  val onPurchasesUpdatedListenerMap = mutableMapOf<UUID, OnPurchasesUpdatedListener>()

  fun addOnPurchaseUpdatedListener(
    requestId: RequestId,
    listener: OnPurchasesUpdatedListener,
  ) {
    purchaseRequestId = requestId
    onPurchasesUpdatedListenerMap[requestId.value] = listener
  }

  fun removeOnPurchaseUpdatedListener(
    requestId: RequestId,
  ) {
    purchaseRequestId = null
    onPurchasesUpdatedListenerMap.remove(requestId.value)
  }

  override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
    val id = purchaseRequestId?.value ?: return
    onPurchasesUpdatedListenerMap[id]?.onPurchasesUpdated(billingResult, purchases)
  }
}

interface OnPurchasesUpdatedListener {
  fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?)
}
