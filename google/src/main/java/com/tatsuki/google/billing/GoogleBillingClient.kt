package com.tatsuki.google.billing

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
import java.lang.ref.WeakReference

interface GoogleBillingClient {

  val isReady: Boolean

  val connectionState: ConnectionState

  fun connect(listener: BillingClientStateListener)

  fun disconnect()

  suspend fun queryProductDetails(params: QueryProductDetailsParams): ProductDetailsResult

  suspend fun queryPurchaseHistory(params: QueryPurchaseHistoryParams): PurchaseHistoryResult

  suspend fun queryPurchases(params: QueryPurchasesParams): PurchasesResult

  fun launchBillingFlow(
    params: BillingFlowParams,
    activity: Activity,
  ): BillingResult

  suspend fun consumePurchase(params: ConsumeParams): ConsumeResult

  suspend fun acknowledgePurchase(params: AcknowledgePurchaseParams): BillingResult

  fun isFeatureSupport(featureType: BillingFeatureType): BillingResult
}
