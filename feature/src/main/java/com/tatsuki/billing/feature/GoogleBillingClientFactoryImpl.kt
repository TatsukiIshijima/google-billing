package com.tatsuki.billing.feature

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.PurchasesUpdatedListener
import com.tatsuki.billing.core.GoogleBillingClient
import com.tatsuki.billing.core.GoogleBillingClientFactory

class GoogleBillingClientFactoryImpl(
  private val context: Context,
) : GoogleBillingClientFactory {

  override fun create(
    enablePendingPurchases: Boolean,
    purchasesUpdatedListener: PurchasesUpdatedListener,
  ): GoogleBillingClient {
    val billingClient = BillingClient.newBuilder(context)
      .setListener(purchasesUpdatedListener)
    if (enablePendingPurchases) {
      billingClient.enablePendingPurchases()
    }
    return GoogleBillingClientImpl(billingClient.build())
  }
}