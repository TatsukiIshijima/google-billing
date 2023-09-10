package com.tatsuki.google.billing

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.ProductDetailsResponseListener
import com.android.billingclient.api.ProductDetailsResult
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.queryProductDetails
import com.tatsuki.google.billing.model.Product

class GoogleBillingClientImpl(
  private val billingClient: BillingClient
) : GoogleBillingClient {

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

  override suspend fun queryProductDetails(params: QueryProductDetailsParams): ProductDetailsResult {
    return billingClient.queryProductDetails(params)
  }

  override suspend fun queryPurchaseHistory() {
    TODO("Not yet implemented")
  }

  override suspend fun queryPurchase() {
    TODO("Not yet implemented")
  }

  override suspend fun consumePurchase() {
    TODO("Not yet implemented")
  }

  override suspend fun acknowledgePurchase() {
    TODO("Not yet implemented")
  }
}
