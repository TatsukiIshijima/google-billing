package com.tatsuki.billing.core

interface GoogleBillingClientFactory {

  fun create(enablePendingPurchases: Boolean = true): GoogleBillingClient
}