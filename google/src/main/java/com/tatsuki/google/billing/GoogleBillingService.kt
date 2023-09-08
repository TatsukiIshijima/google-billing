package com.tatsuki.google.billing

interface GoogleBillingService {

  suspend fun connect()

  suspend fun disconnect()

  suspend fun queryProductDetails()

  suspend fun queryPurchases()

  suspend fun purchase()
}
