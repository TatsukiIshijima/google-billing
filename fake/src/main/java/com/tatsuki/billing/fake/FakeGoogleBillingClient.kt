package com.tatsuki.billing.fake

import android.app.Activity
import com.android.billingclient.api.AcknowledgePurchaseParams
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
import com.tatsuki.billing.core.GoogleBillingClient

class FakeGoogleBillingClient: GoogleBillingClient {

  override val isReady: Boolean
    get() = TODO("Not yet implemented")

  override fun connect(listener: BillingClientStateListener) {
    TODO("Not yet implemented")
  }

  override fun disconnect() {
    TODO("Not yet implemented")
  }

  override suspend fun queryProductDetails(params: QueryProductDetailsParams): ProductDetailsResult {
    TODO("Not yet implemented")
  }

  override suspend fun queryPurchaseHistory(params: QueryPurchaseHistoryParams): PurchaseHistoryResult {
    TODO("Not yet implemented")
  }

  override suspend fun queryPurchases(params: QueryPurchasesParams): PurchasesResult {
    TODO("Not yet implemented")
  }

  override fun launchBillingFlow(params: BillingFlowParams, activity: Activity): BillingResult {
    TODO("Not yet implemented")
  }

  override suspend fun consumePurchase(params: ConsumeParams): ConsumeResult {
    TODO("Not yet implemented")
  }

  override suspend fun acknowledgePurchase(params: AcknowledgePurchaseParams): BillingResult {
    TODO("Not yet implemented")
  }

  override fun isFeatureSupport(featureType: String): BillingResult {
    TODO("Not yet implemented")
  }
}