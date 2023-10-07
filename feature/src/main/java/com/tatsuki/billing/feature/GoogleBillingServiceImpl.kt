package com.tatsuki.billing.feature

import android.app.Activity
import android.util.Log
import androidx.annotation.VisibleForTesting
import com.android.billingclient.api.AccountIdentifiers
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingFlowParams.ProductDetailsParams
import com.android.billingclient.api.BillingFlowParams.SubscriptionUpdateParams
import com.android.billingclient.api.BillingFlowParams.SubscriptionUpdateParams.ReplacementMode
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchaseHistoryRecord
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchaseHistoryParams
import com.android.billingclient.api.QueryPurchasesParams
import com.tatsuki.billing.core.GoogleBillingClientFactory
import com.tatsuki.billing.feature.listener.ConnectionStateListener
import com.tatsuki.billing.feature.listener.OnBillingServiceConnectionListener
import com.tatsuki.billing.feature.listener.OnPurchasesUpdatedListener
import com.tatsuki.billing.feature.listener.PurchasesListener
import com.tatsuki.billing.feature.model.Product
import com.tatsuki.billing.feature.model.RequestId
import com.tatsuki.billing.feature.model.type.ConnectionState
import com.tatsuki.billing.feature.model.type.ProductType
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.ref.WeakReference
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class GoogleBillingServiceImpl(
  googleBillingFactory: GoogleBillingClientFactory,
) : GoogleBillingService {

  private val billingClient = googleBillingFactory.create()

  @VisibleForTesting
  val connectionListener = ConnectionStateListener

  @VisibleForTesting
  val purchasesListener = PurchasesListener

  override suspend fun connect(): ConnectionState {
    if (billingClient.isReady) {
      return ConnectionState.CONNECTED
    }

    return suspendCancellableCoroutine { continuation ->

      val requestId = RequestId().value

      connectionListener.addOnBillingServiceConnectionListener(
        requestId = requestId,
        listener = object : OnBillingServiceConnectionListener {
          override fun onBillingSetupFinished(billingResult: BillingResult) {
            connectionListener.removeOnBillingServiceConnectionListener(requestId)
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
              continuation.resume(ConnectionState.CONNECTED)
            } else {
              continuation.resumeWithException(billingResult.responseCode.toException())
            }
          }

          override fun onBillingServiceDisconnected() {
            connectionListener.removeOnBillingServiceConnectionListener(requestId)
            continuation.resume(ConnectionState.DISCONNECTED)
          }
        }
      )

      continuation.invokeOnCancellation {
        endConnectionIfReady()
      }

      billingClient.connect(connectionListener)
    }
  }

  // TODO:Impl retry connect
  // https://developer.android.com/google/play/billing/errors

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

  private fun launchConsumableProductBillingFlow(
    productDetails: ProductDetails,
    activityRef: WeakReference<Activity>,
  ): BillingResult {

    val productDetailsParams = ProductDetailsParams.newBuilder()
      .setProductDetails(productDetails)
      .build()

    val productDetailsParamsList = listOf(productDetailsParams)
    val billingFlowParams = BillingFlowParams.newBuilder()
      .setProductDetailsParamsList(productDetailsParamsList)
      .build()

    return billingClient.launchBillingFlow(
      params = billingFlowParams,
      activity = activityRef.get()!!
    )
  }

  override suspend fun purchaseConsumableProduct(
    productDetails: ProductDetails,
    activityRef: WeakReference<Activity>,
  ): List<Purchase>? {
    return suspendCancellableCoroutine { continuation ->
      val launchBillingFlowTask = launchConsumableProductBillingFlow(
        productDetails = productDetails,
        activityRef = activityRef
      )
      handleLaunchBillingFlowResult(
        billingResult = launchBillingFlowTask,
        onSuccess = { purchases -> continuation.resume(purchases) },
        onFailure = { e -> continuation.resumeWithException(e) }
      )
    }
  }

  private fun launchSubscriptionBillingFlow(
    productDetails: ProductDetails,
    offerToken: String,
    activityRef: WeakReference<Activity>,
    obfuscatedAccountId: String?,
    obfuscatedProfileId: String?,
    oldPurchaseToken: String?,
    subscriptionReplacementMode: Int,
  ): BillingResult {
    val productDetailsParams = ProductDetailsParams.newBuilder()
      .setProductDetails(productDetails)
      .setOfferToken(offerToken)
      .build()

    val productDetailsParamsList = listOf(productDetailsParams)

    val billingFlowParams = BillingFlowParams.newBuilder()
      .setProductDetailsParamsList(productDetailsParamsList)

    if (obfuscatedAccountId?.isNotEmpty() == true) {
      billingFlowParams.setObfuscatedAccountId(obfuscatedAccountId)
    }
    if (obfuscatedProfileId?.isNotEmpty() == true) {
      billingFlowParams.setObfuscatedProfileId(obfuscatedProfileId)
    }
    val subscriptionUpdateParams = if (oldPurchaseToken?.isNotEmpty() == true) {
      SubscriptionUpdateParams.newBuilder()
        .setOldPurchaseToken(oldPurchaseToken)
        .setSubscriptionReplacementMode(subscriptionReplacementMode)
        .build()
    } else {
      null
    }
    if (subscriptionUpdateParams != null) {
      Log.d("GoogleBillingService", "Launch update subscription billing flow. replaceMode=$subscriptionReplacementMode")
      billingFlowParams.setSubscriptionUpdateParams(subscriptionUpdateParams)
    } else {
      Log.d("GoogleBillingService", "Launch new subscription billing flow.")
    }
    return billingClient.launchBillingFlow(
      params = billingFlowParams.build(),
      activity = activityRef.get()!!
    )
  }

  override suspend fun purchaseSubscription(
    productDetails: ProductDetails,
    offerToken: String,
    activityRef: WeakReference<Activity>,
    accountIdentifiers: AccountIdentifiers?,
    oldPurchaseToken: String?,
    @ReplacementMode subscriptionReplacementMode: Int,
  ): List<Purchase> {
    return suspendCancellableCoroutine { continuation ->
      val launchBillingFlowTask = launchSubscriptionBillingFlow(
        productDetails = productDetails,
        offerToken = offerToken,
        activityRef = activityRef,
        obfuscatedAccountId = accountIdentifiers?.obfuscatedAccountId,
        obfuscatedProfileId = accountIdentifiers?.obfuscatedProfileId,
        oldPurchaseToken = oldPurchaseToken,
        subscriptionReplacementMode = subscriptionReplacementMode,
      )
      handleLaunchBillingFlowResult(
        billingResult = launchBillingFlowTask,
        onSuccess = { purchases -> continuation.resume(purchases) },
        onFailure = { e -> continuation.resumeWithException(e) }
      )
    }
  }

  private fun handleLaunchBillingFlowResult(
    billingResult: BillingResult,
    onSuccess: (purchases: List<Purchase>) -> Unit,
    onFailure: (e: GoogleBillingServiceException) -> Unit,
  ) {
    if (billingResult.responseCode == BillingResponseCode.OK) {
      val requestId = RequestId()
      purchasesListener.addOnPurchaseUpdatedListener(
        requestId = requestId,
        listener = object : OnPurchasesUpdatedListener {
          override fun onPurchasesUpdated(
            billingResult: BillingResult,
            purchases: MutableList<Purchase>?,
          ) {
            purchasesListener.removeOnPurchaseUpdatedListener(requestId)
            if (billingResult.responseCode == BillingResponseCode.OK) {
              onSuccess(purchases ?: emptyList())
            } else {
              onFailure(billingResult.responseCode.toException())
            }
          }
        }
      )
    } else {
      onFailure(billingResult.responseCode.toException())
    }
  }

  override suspend fun consumePurchase(purchaseToken: String) {
    val consumeParams = ConsumeParams.newBuilder()
      .setPurchaseToken(purchaseToken)
      .build()
    val consumeTask = billingClient.consumePurchase(consumeParams)
    if (consumeTask.billingResult.responseCode == BillingResponseCode.OK) {
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
    if (acknowledgeTask.responseCode == BillingResponseCode.OK) {
      // no-op
    } else {
      throw acknowledgeTask.responseCode.toException()
    }
  }
}
