package com.tatsuki.google.billing

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener

class GoogleBillingClientImpl(
  private val billingClient: BillingClient
): GoogleBillingClient {

  override val isReady: Boolean
    get() = billingClient.isReady

  override val connectionState: ConnectionState
    get() = ConnectionState.from(billingClient.connectionState)

  override fun connect(listener: BillingClientStateListener) {
    billingClient.startConnection(listener)
  }

  override fun disconnect() {
    billingClient.endConnection()
  }

  override fun queryProductDetails() {
    TODO("Not yet implemented")
  }

  override fun queryPurchases() {
    TODO("Not yet implemented")
  }

  override fun purchase() {
    TODO("Not yet implemented")
  }
}
