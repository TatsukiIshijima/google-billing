package com.tatsuki.google.billing

import com.android.billingclient.api.ProductDetails
import com.tatsuki.google.billing.model.Product

interface GoogleBillingService {

  suspend fun connect(): ConnectionState

  fun disconnect()

  suspend fun queryProductDetails(products: List<Product>): List<ProductDetails>?

  suspend fun queryPurchaseHistory()

  suspend fun queryPurchase()

  suspend fun consumePurchase()

  suspend fun acknowledgePurchase()
}
