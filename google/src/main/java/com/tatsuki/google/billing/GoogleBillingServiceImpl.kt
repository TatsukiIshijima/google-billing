package com.tatsuki.google.billing

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class GoogleBillingServiceImpl(
  private val billingClient: BillingClient,
) : GoogleBillingService {

  private val connectionListener = ConnectionStateListener

  override suspend fun connect(): ConnectionState {
    return suspendCancellableCoroutine { continuation ->

      val requestId = UUID.randomUUID()

      connectionListener.addOnBillingServiceConnectionListener(
        requestId = requestId,
        listener = object : OnBillingServiceConnectionListener {
          override fun onBillingSetupFinished(billingResult: BillingResult) {
            connectionListener.clearOnBillingServiceConnectionListener()
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
              val connectionState = ConnectionState.from(billingClient.connectionState)
              continuation.resume(connectionState)
            } else {
              continuation.resumeWithException(Exception(""))
            }
          }

          override fun onBillingServiceDisconnected() {
            connectionListener.clearOnBillingServiceConnectionListener()
            val connectionState = ConnectionState.from(billingClient.connectionState)
            continuation.resume(connectionState)
          }
        }
      )

      continuation.invokeOnCancellation {
        endConnectionIfReady()
      }

      billingClient.startConnection(connectionListener)
    }
  }

  override fun disconnect() {
    endConnectionIfReady()
  }

  private fun endConnectionIfReady() {
    if (billingClient.isReady) {
      billingClient.endConnection()
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
