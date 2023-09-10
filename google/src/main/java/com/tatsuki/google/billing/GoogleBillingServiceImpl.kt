package com.tatsuki.google.billing

import androidx.annotation.VisibleForTesting
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.QueryProductDetailsParams
import com.tatsuki.google.billing.GoogleBillingServiceException.ConnectFailedException
import com.tatsuki.google.billing.model.Product
import com.tatsuki.google.billing.model.RequestId
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class GoogleBillingServiceImpl(
  private val billingClient: GoogleBillingClient,
) : GoogleBillingService {

  @VisibleForTesting
  val connectionListener = ConnectionStateListener

  override suspend fun connect(): ConnectionState {
    if (billingClient.isReady) {
      return billingClient.connectionState
    }

    return suspendCancellableCoroutine { continuation ->

      val requestId = RequestId().value

      connectionListener.addOnBillingServiceConnectionListener(
        requestId = requestId,
        listener = object : OnBillingServiceConnectionListener {
          override fun onBillingSetupFinished(billingResult: BillingResult) {
            connectionListener.removeOnBillingServiceConnectionListener(requestId)
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
              continuation.resume(billingClient.connectionState)
            } else {
              continuation.resumeWithException(
                ConnectFailedException(
                  responseCode = billingResult.responseCode
                )
              )
            }
          }

          override fun onBillingServiceDisconnected() {
            connectionListener.removeOnBillingServiceConnectionListener(requestId)
            continuation.resume(billingClient.connectionState)
          }
        }
      )

      continuation.invokeOnCancellation {
        endConnectionIfReady()
      }

      billingClient.connect(connectionListener)
    }
  }

  override fun disconnect() {
    endConnectionIfReady()
  }

  private fun endConnectionIfReady() {
    if (billingClient.isReady) {
      billingClient.disconnect()
    }
  }

  override suspend fun queryProductDetails(products: List<Product>): List<ProductDetails>? {
    val queryProductDetailsParams = QueryProductDetailsParams.newBuilder()
      .setProductList(products.map { it.toQueryProduct() })
      .build()
    val queryProductDetailsTask = billingClient.queryProductDetails(queryProductDetailsParams)
    return if (queryProductDetailsTask.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
      queryProductDetailsTask.productDetailsList
    } else {
      null
    }
  }

  override suspend fun queryPurchaseHistory() {
//    TODO("Not yet implemented")
  }

  override suspend fun queryPurchase() {
//    TODO("Not yet implemented")
  }

  override suspend fun consumePurchase() {
//    TODO("Not yet implemented")
  }

  override suspend fun acknowledgePurchase() {
//    TODO("Not yet implemented")
  }
}
