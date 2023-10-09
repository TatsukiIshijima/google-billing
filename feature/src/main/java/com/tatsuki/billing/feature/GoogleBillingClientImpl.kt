package com.tatsuki.billing.feature

import android.app.Activity
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.FeatureType
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ConsumeResult
import com.android.billingclient.api.ProductDetailsResult
import com.android.billingclient.api.PurchaseHistoryResult
import com.android.billingclient.api.PurchasesResult
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchaseHistoryParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.acknowledgePurchase
import com.android.billingclient.api.consumePurchase
import com.android.billingclient.api.queryProductDetails
import com.android.billingclient.api.queryPurchaseHistory
import com.android.billingclient.api.queryPurchasesAsync
import com.tatsuki.billing.core.GoogleBillingClient

class GoogleBillingClientImpl(
  private val billingClient: BillingClient,
) : GoogleBillingClient {

  override val isReady: Boolean
    get() = billingClient.isReady

  override fun connect(listener: BillingClientStateListener) {
    billingClient.startConnection(listener)
  }

  override fun disconnect() {
    billingClient.endConnection()
  }

  override suspend fun queryProductDetails(params: QueryProductDetailsParams): ProductDetailsResult {
    return billingClient.queryProductDetails(params)
  }

  override suspend fun queryPurchaseHistory(params: QueryPurchaseHistoryParams): PurchaseHistoryResult {
    return billingClient.queryPurchaseHistory(params)
  }

  override suspend fun queryPurchases(params: QueryPurchasesParams): PurchasesResult {
    return billingClient.queryPurchasesAsync(params)
  }

  override fun launchBillingFlow(
    params: BillingFlowParams,
    activity: Activity,
  ): BillingResult {
    return billingClient.launchBillingFlow(activity, params)
  }

  override suspend fun consumePurchase(params: ConsumeParams): ConsumeResult {
    return billingClient.consumePurchase(params)
  }

  override suspend fun acknowledgePurchase(params: AcknowledgePurchaseParams): BillingResult {
    return billingClient.acknowledgePurchase(params)
  }

  override fun isFeatureSupport(@FeatureType featureType: String): BillingResult {
    return billingClient.isFeatureSupported(featureType)
  }
}
