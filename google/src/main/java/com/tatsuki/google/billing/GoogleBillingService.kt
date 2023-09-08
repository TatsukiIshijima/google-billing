package com.tatsuki.google.billing

interface GoogleBillingService {

  suspend fun connect(): ConnectionState

  fun disconnect()

  suspend fun queryProductDetails()

  suspend fun queryPurchases()

  suspend fun purchase()
}
