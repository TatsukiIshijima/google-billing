package com.tatsuki.google.billing

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.PurchasesUpdatedListener
import com.tatsuki.billing.core.GoogleBillingClient
import com.tatsuki.billing.core.GoogleBillingClientFactory

class GoogleBillingClientFactoryImpl(
  private val context: Context,
  private val purchasesUpdatedListener: PurchasesUpdatedListener,
) : GoogleBillingClientFactory {

  override fun create(enablePendingPurchases: Boolean): GoogleBillingClient {
    val billingClient = BillingClient.newBuilder(context)
      .setListener(purchasesUpdatedListener)
    if (enablePendingPurchases) {
      billingClient.enablePendingPurchases()
    }
    return GoogleBillingClientImpl(billingClient.build())
  }
}