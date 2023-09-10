package com.tatsuki.google.billing

import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.PurchaseHistoryRecord
import com.tatsuki.google.billing.model.Product
import com.tatsuki.google.billing.model.ProductType

interface GoogleBillingService {

  suspend fun connect(): ConnectionState

  fun disconnect()

  suspend fun queryProductDetails(products: List<Product>): List<ProductDetails>?

  suspend fun queryPurchaseHistory(productType: ProductType): List<PurchaseHistoryRecord>?

  suspend fun queryPurchase()

  suspend fun consumePurchase()

  suspend fun acknowledgePurchase()
}
