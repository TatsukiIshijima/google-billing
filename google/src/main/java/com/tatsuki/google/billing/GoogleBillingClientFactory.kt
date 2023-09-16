package com.tatsuki.google.billing

interface GoogleBillingClientFactory {

  fun create(enablePendingPurchases: Boolean = true): GoogleBillingClient
}