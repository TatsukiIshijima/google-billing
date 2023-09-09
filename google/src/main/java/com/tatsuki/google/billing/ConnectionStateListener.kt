package com.tatsuki.google.billing

import androidx.annotation.VisibleForTesting
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import java.util.UUID

object ConnectionStateListener : BillingClientStateListener {

  @VisibleForTesting
  var connectionRequestId: UUID? = null

  @VisibleForTesting
  val onBillingServiceConnectionListenerMap = mutableMapOf<UUID, OnBillingServiceConnectionListener>()

  fun addOnBillingServiceConnectionListener(
    requestId: UUID,
    listener: OnBillingServiceConnectionListener
  ) {
    connectionRequestId = requestId
    onBillingServiceConnectionListenerMap[requestId] = listener
  }

  fun removeOnBillingServiceConnectionListener(
    requestId: UUID
  ) {
    connectionRequestId = null
    onBillingServiceConnectionListenerMap.remove(requestId)
  }

  override fun onBillingSetupFinished(billingResult: BillingResult) {
    onBillingServiceConnectionListenerMap[connectionRequestId]?.onBillingSetupFinished(billingResult)
  }

  override fun onBillingServiceDisconnected() {
    onBillingServiceConnectionListenerMap[connectionRequestId]?.onBillingServiceDisconnected()
  }
}

interface OnBillingServiceConnectionListener {
  fun onBillingSetupFinished(billingResult: BillingResult)
  fun onBillingServiceDisconnected()
}
