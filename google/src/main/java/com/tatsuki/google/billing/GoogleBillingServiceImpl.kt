package com.tatsuki.google.billing

import androidx.annotation.VisibleForTesting
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.tatsuki.google.billing.model.RequestId
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class GoogleBillingServiceImpl(
  private val billingClient: GoogleBillingClient
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
              // TODO: Add exception
              continuation.resumeWithException(Exception(""))
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

  override suspend fun queryProductDetails() {
//    TODO("Not yet implemented")
  }

  override suspend fun queryPurchases() {
//    TODO("Not yet implemented")
  }

  override suspend fun purchase() {
//    TODO("Not yet implemented")
  }
}
