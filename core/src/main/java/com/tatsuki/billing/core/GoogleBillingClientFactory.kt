package com.tatsuki.billing.core

import com.android.billingclient.api.PurchasesUpdatedListener

interface GoogleBillingClientFactory {

  fun create(
    enablePendingPurchases: Boolean = true,
    purchasesUpdatedListener: PurchasesUpdatedListener,
  ): GoogleBillingClient
}