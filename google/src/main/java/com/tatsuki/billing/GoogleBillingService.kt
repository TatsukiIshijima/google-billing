package com.tatsuki.billing

import android.app.Activity
import com.android.billingclient.api.AccountIdentifiers
import com.android.billingclient.api.BillingFlowParams.SubscriptionUpdateParams.ReplacementMode
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchaseHistoryRecord
import com.tatsuki.billing.model.Product
import com.tatsuki.billing.model.type.ConnectionState
import com.tatsuki.billing.model.type.ProductType
import java.lang.ref.WeakReference

interface GoogleBillingService {

  suspend fun connect(): ConnectionState

  fun disconnect()

  suspend fun queryProductDetails(products: List<Product>): List<ProductDetails>

  suspend fun queryPurchaseHistory(productType: ProductType): List<PurchaseHistoryRecord>

  suspend fun queryPurchases(productType: ProductType): List<Purchase>

  suspend fun purchaseConsumableProduct(
    productDetails: ProductDetails,
    activityRef: WeakReference<Activity>,
  ): List<Purchase>?

  suspend fun purchaseSubscription(
    productDetails: ProductDetails,
    offerToken: String,
    activityRef: WeakReference<Activity>,
    accountIdentifiers: AccountIdentifiers? = null,
    oldPurchaseToken: String? = null,
    @ReplacementMode subscriptionReplacementMode: Int = ReplacementMode.WITH_TIME_PRORATION,
  ): List<Purchase>?

  suspend fun consumePurchase(purchaseToken: String)

  suspend fun acknowledgePurchase(purchaseToken: String)
}
