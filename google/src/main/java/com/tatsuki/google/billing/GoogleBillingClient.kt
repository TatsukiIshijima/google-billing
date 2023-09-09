package com.tatsuki.google.billing

import com.android.billingclient.api.BillingClientStateListener

interface GoogleBillingClient {

  val isReady: Boolean

  val connectionState: ConnectionState

  fun connect(listener: BillingClientStateListener)

  fun disconnect()

  fun queryProductDetails()

  fun queryPurchases()

  fun purchase()
}
