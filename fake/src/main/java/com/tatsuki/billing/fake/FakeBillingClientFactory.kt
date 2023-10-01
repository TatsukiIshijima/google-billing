package com.tatsuki.billing.fake

import com.tatsuki.billing.core.GoogleBillingClient
import com.tatsuki.billing.core.GoogleBillingClientFactory

class FakeBillingClientFactory : GoogleBillingClientFactory {

  lateinit var fakeGoogleBillingClient: FakeGoogleBillingClient
    private set

  override fun create(enablePendingPurchases: Boolean): GoogleBillingClient {
    val fakeGoogleBillingClient = FakeGoogleBillingClient()
    this.fakeGoogleBillingClient = fakeGoogleBillingClient
    return fakeGoogleBillingClient
  }
}