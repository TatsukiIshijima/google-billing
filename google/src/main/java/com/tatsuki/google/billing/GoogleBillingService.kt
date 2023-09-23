package com.tatsuki.google.billing

import android.app.Activity
import com.android.billingclient.api.BillingFlowParams.SubscriptionUpdateParams.ReplacementMode
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchaseHistoryRecord
import com.tatsuki.google.billing.model.Product
import com.tatsuki.google.billing.model.type.ConnectionState
import com.tatsuki.google.billing.model.type.ProductType

interface GoogleBillingService {

  suspend fun connect(): ConnectionState

  fun disconnect()

  suspend fun queryProductDetails(products: List<Product>): List<ProductDetails>

  suspend fun queryPurchaseHistory(productType: ProductType): List<PurchaseHistoryRecord>

  suspend fun queryPurchases(productType: ProductType): List<Purchase>

  suspend fun purchaseConsumableProduct(
    productDetails: ProductDetails,
    activity: Activity,
  ): List<Purchase>?

  suspend fun purchaseSubscription(
    productDetails: ProductDetails,
    offerToken: String,
    activity: Activity,
    obfuscatedAccountId: String? = null,
    obfuscatedProfileId: String? = null,
    oldPurchaseToken: String? = null,
    subscriptionReplacementMode: Int = ReplacementMode.WITH_TIME_PRORATION,
  ): List<Purchase>?

  suspend fun consumePurchase(purchaseToken: String)

  suspend fun acknowledgePurchase(purchaseToken: String)
}
