package com.tatsuki.google.billing

import com.android.billingclient.api.BillingClient

class GoogleBillingServiceImpl(
  billingClient: BillingClient
) : GoogleBillingService {

  override suspend fun connect() {
    TODO("Not yet implemented")
  }

  override suspend fun disconnect() {
    TODO("Not yet implemented")
  }

  override suspend fun queryProductDetails() {
    TODO("Not yet implemented")
  }

  override suspend fun queryPurchases() {
    TODO("Not yet implemented")
  }

  override suspend fun purchase() {
    TODO("Not yet implemented")
  }
}
