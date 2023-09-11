package com.tatsuki.google.billing

import androidx.annotation.VisibleForTesting
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchaseHistoryRecord
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchaseHistoryParams
import com.android.billingclient.api.QueryPurchasesParams
import com.tatsuki.google.billing.model.Product
import com.tatsuki.google.billing.model.ProductType
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
              continuation.resumeWithException(billingResult.responseCode.toException())
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

  override suspend fun queryProductDetails(products: List<Product>): List<ProductDetails> {
    val queryProductDetailsParams = QueryProductDetailsParams.newBuilder()
      .setProductList(products.map { it.toQueryProduct() })
      .build()
    val queryProductDetailsTask = billingClient.queryProductDetails(queryProductDetailsParams)
    return if (queryProductDetailsTask.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
      queryProductDetailsTask.productDetailsList ?: emptyList()
    } else {
      throw queryProductDetailsTask.billingResult.responseCode.toException()
    }
  }

  override suspend fun queryPurchaseHistory(productType: ProductType): List<PurchaseHistoryRecord> {
    val queryPurchaseHistoryParams = QueryPurchaseHistoryParams.newBuilder()
      .setProductType(productType.value)
      .build()
    val queryPurchaseHistoryTask = billingClient.queryPurchaseHistory(queryPurchaseHistoryParams)
    return if (queryPurchaseHistoryTask.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
      queryPurchaseHistoryTask.purchaseHistoryRecordList ?: emptyList()
    } else {
      throw queryPurchaseHistoryTask.billingResult.responseCode.toException()
    }
  }

  override suspend fun queryPurchases(productType: ProductType): List<Purchase> {
    val queryPurchasesParams = QueryPurchasesParams.newBuilder()
      .setProductType(productType.value)
      .build()
    val queryPurchaseTask = billingClient.queryPurchases(queryPurchasesParams)
    return if (queryPurchaseTask.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
      queryPurchaseTask.purchasesList
    } else {
      throw queryPurchaseTask.billingResult.responseCode.toException()
    }
  }

  override suspend fun consumePurchase(purchaseToken: String) {
    val consumeParams = ConsumeParams.newBuilder()
      .setPurchaseToken(purchaseToken)
      .build()
    val consumeTask = billingClient.consumePurchase(consumeParams)
    if (consumeTask.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
      // no-op
    } else {
      throw consumeTask.billingResult.responseCode.toException()
    }
  }

  override suspend fun acknowledgePurchase(purchaseToken: String) {
    val acknowledgeParams = AcknowledgePurchaseParams.newBuilder()
      .setPurchaseToken(purchaseToken)
      .build()
    val acknowledgeTask = billingClient.acknowledgePurchase(acknowledgeParams)
    if (acknowledgeTask.responseCode == BillingClient.BillingResponseCode.OK) {
      // no-op
    } else {
      throw acknowledgeTask.responseCode.toException()
    }
  }
}
