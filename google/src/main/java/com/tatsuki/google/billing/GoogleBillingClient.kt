package com.tatsuki.google.billing

import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.ProductDetailsResult
import com.android.billingclient.api.QueryProductDetailsParams

interface GoogleBillingClient {

  val isReady: Boolean

  val connectionState: ConnectionState

  fun connect(listener: BillingClientStateListener)

  fun disconnect()

  suspend fun queryProductDetails(params: QueryProductDetailsParams): ProductDetailsResult

  fun queryPurchases()

  fun purchase()
}
