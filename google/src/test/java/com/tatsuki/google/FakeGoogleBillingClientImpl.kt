package com.tatsuki.google

import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetailsResult
import com.android.billingclient.api.QueryProductDetailsParams
import com.tatsuki.google.billing.ConnectionState
import com.tatsuki.google.billing.GoogleBillingClient

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

  sealed interface ConnectionPattern {
    val isReady: Boolean
    val connectionState: ConnectionState

    sealed interface Connect : ConnectionPattern {
      val billingResult: BillingResult

      data class NotConnectedYet(
        override val isReady: Boolean = false,
        override val connectionState: ConnectionState = ConnectionState.CONNECTED,
        override val billingResult: BillingResult =
          BillingResult
            .newBuilder()
            .setResponseCode(BillingResponseCode.OK)
            .build(),
      ) : Connect

      data class AlreadyConnected(
        override val isReady: Boolean = true,
        override val connectionState: ConnectionState = ConnectionState.CONNECTED,
        override val billingResult: BillingResult =
          BillingResult
            .newBuilder()
            .setResponseCode(BillingResponseCode.OK)
            .build(),
      ) : Connect

      data class ConnectFailed(
        override val isReady: Boolean = false,
        override val connectionState: ConnectionState = ConnectionState.DISCONNECTED,
        override val billingResult: BillingResult =
          BillingResult
            .newBuilder()
            .setResponseCode(BillingResponseCode.SERVICE_UNAVAILABLE)
            .build(),
      ) : Connect
    }

    sealed interface Disconnect : ConnectionPattern {
      data class NotDisConnectedYet(
        override val isReady: Boolean = true,
        override val connectionState: ConnectionState = ConnectionState.DISCONNECTED,
      ) : Disconnect

      data class AlreadyDisconnected(
        override val isReady: Boolean = false,
        override val connectionState: ConnectionState = ConnectionState.DISCONNECTED,
      ) : Disconnect
    }
  }

  private lateinit var connectionPattern: ConnectionPattern

  fun setup(pattern: ConnectionPattern) {
    this.connectionPattern = pattern
  }

  sealed interface QueryProductDetailsPattern {
    val result: ProductDetailsResult

    data class Success(
      override val result: ProductDetailsResult =
        ProductDetailsResult(
          billingResult = BillingResult.newBuilder()
            .setResponseCode(BillingResponseCode.OK)
            .build(),
          productDetailsList = listOf()
        )
    ) : QueryProductDetailsPattern

    data class Failure(
      override val result: ProductDetailsResult =
        ProductDetailsResult(
          billingResult = BillingResult.newBuilder()
            .setResponseCode(BillingResponseCode.ERROR)
            .build(),
          productDetailsList = null
        )
    ) : QueryProductDetailsPattern
  }

  private lateinit var queryProductDetailsPattern: QueryProductDetailsPattern

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

  override fun queryPurchases() {
    TODO("Not yet implemented")
  }

  override fun purchase() {
    TODO("Not yet implemented")
  }
}
