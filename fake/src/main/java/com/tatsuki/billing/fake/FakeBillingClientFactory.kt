package com.tatsuki.billing.fake

import com.android.billingclient.api.PurchasesUpdatedListener
import com.tatsuki.billing.core.GoogleBillingClient
import com.tatsuki.billing.core.GoogleBillingClientFactory

class FakeBillingClientFactory : GoogleBillingClientFactory {

  lateinit var fakeGoogleBillingClient: FakeGoogleBillingClient
    private set

  override fun create(
    enablePendingPurchases: Boolean,
    purchasesUpdatedListener: PurchasesUpdatedListener,
  ): GoogleBillingClient {
    val fakeGoogleBillingClient = FakeGoogleBillingClient(purchasesUpdatedListener)
    this.fakeGoogleBillingClient = fakeGoogleBillingClient
    return fakeGoogleBillingClient
  }
}