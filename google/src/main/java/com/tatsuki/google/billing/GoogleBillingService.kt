package com.tatsuki.google.billing

import android.app.Activity
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchaseHistoryRecord
import com.tatsuki.google.billing.model.Product
import com.tatsuki.google.billing.model.ProductType
import java.lang.ref.WeakReference

interface GoogleBillingService {

  suspend fun connect(): ConnectionState

  fun disconnect()

  suspend fun queryProductDetails(products: List<Product>): List<ProductDetails>

  suspend fun queryPurchaseHistory(productType: ProductType): List<PurchaseHistoryRecord>

  suspend fun queryPurchases(productType: ProductType): List<Purchase>

  suspend fun purchase(
    productDetails: ProductDetails,
    offerToken: String?,
    activity: Activity,
  ): List<Purchase>?

  suspend fun consumePurchase(purchaseToken: String)

  suspend fun acknowledgePurchase(purchaseToken: String)
}
