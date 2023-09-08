package com.tatsuki.google.billing

import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import java.util.UUID

internal object ConnectionStateListener : BillingClientStateListener {

  private var connectionRequestId: UUID? = null
  private val onBillingServiceConnectionListenerMap = mutableMapOf<UUID, OnBillingServiceConnectionListener>()

  fun addOnBillingServiceConnectionListener(
    requestId: UUID,
    listener: OnBillingServiceConnectionListener
  ) {
    if (connectionRequestId == null) {
      connectionRequestId = requestId
    }
    onBillingServiceConnectionListenerMap[requestId] = listener
  }

  fun clearOnBillingServiceConnectionListener() {
    onBillingServiceConnectionListenerMap.clear()
    connectionRequestId = null
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
