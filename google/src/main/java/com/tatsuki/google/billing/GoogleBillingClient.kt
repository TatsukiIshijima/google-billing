package com.tatsuki.google.billing

import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.ProductDetailsResult
import com.android.billingclient.api.PurchaseHistoryResult
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchaseHistoryParams

interface GoogleBillingClient {

  val isReady: Boolean

  val connectionState: ConnectionState

  fun connect(listener: BillingClientStateListener)

  fun disconnect()

  suspend fun queryProductDetails(params: QueryProductDetailsParams): ProductDetailsResult

  suspend fun queryPurchaseHistory(params: QueryPurchaseHistoryParams): PurchaseHistoryResult

  suspend fun queryPurchase()

  suspend fun consumePurchase()

  suspend fun acknowledgePurchase()
}
