package com.tatsuki.google

import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.ProductDetailsResult
import com.android.billingclient.api.PurchaseHistoryResult
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchaseHistoryParams
import com.tatsuki.google.billing.ConnectionState
import com.tatsuki.google.billing.GoogleBillingClient
import com.tatsuki.google.billing.pattern.ConnectionPattern
import com.tatsuki.google.billing.pattern.QueryProductDetailsPattern

class FakeGoogleBillingClientImpl : GoogleBillingClient {

  private lateinit var billingClientStateListener: BillingClientStateListener

  data class ConnectionCallCounter(
    var connectCallCount: Int = 0,
    var disconnectCallCount: Int = 0,
  ) {
    fun reset() {
      connectCallCount = 0
      disconnectCallCount = 0
    }
  }

  val connectionCallCounter = ConnectionCallCounter()

  private lateinit var connectionPattern: ConnectionPattern
  private lateinit var queryProductDetailsPattern: QueryProductDetailsPattern

  fun setup(pattern: ConnectionPattern) {
    this.connectionPattern = pattern
  }

  fun setup(pattern: QueryProductDetailsPattern) {
    this.queryProductDetailsPattern = pattern
  }

  override val isReady: Boolean
    get() = connectionPattern.isReady
  override val connectionState: ConnectionState
    get() = connectionPattern.connectionState

  override fun connect(listener: BillingClientStateListener) {
    connectionCallCounter.connectCallCount++
    billingClientStateListener = listener
  }

  fun onBillingSetupFinished() {
    if (this.connectionPattern is ConnectionPattern.Connect) {
      val billingResult = (this.connectionPattern as ConnectionPattern.Connect).billingResult
      billingClientStateListener.onBillingSetupFinished(billingResult)
    }
  }

  override fun disconnect() {
    connectionCallCounter.disconnectCallCount++
  }

  override suspend fun queryProductDetails(params: QueryProductDetailsParams): ProductDetailsResult {
    return queryProductDetailsPattern.result
  }

  override suspend fun queryPurchaseHistory(params: QueryPurchaseHistoryParams): PurchaseHistoryResult {
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
