package com.tatsuki.google.billing

class FakeGoogleBillingClientFactoryImpl() : GoogleBillingClientFactory {

  lateinit var fakeGoogleBillingClient: FakeGoogleBillingClientImpl
    private set

  override fun create(enablePendingPurchases: Boolean): GoogleBillingClient {
    val fakeGoogleBillingClient = FakeGoogleBillingClientImpl()
    this.fakeGoogleBillingClient = fakeGoogleBillingClient
    return fakeGoogleBillingClient
  }
}