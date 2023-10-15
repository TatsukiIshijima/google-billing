package com.tatsuki.inappbilling

import android.app.Activity
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchaseHistoryRecord
import com.tatsuki.billing.feature.model.type.ConnectionState
import com.tatsuki.billing.feature.GoogleBillingService
import com.tatsuki.billing.feature.GoogleBillingServiceException
import com.tatsuki.billing.feature.model.type.ProductType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

/**
 * https://github.com/android/play-billing-samples/blob/main/ClassyTaxiAppKotlin/app/src/main/java/com/example/billing/gpbl/BillingClientLifecycle.kt
 */

@Singleton
class BillingClientLifecycle @Inject constructor(
  private val googleBillingService: GoogleBillingService,
  private val coroutineScope: CoroutineScope =
    CoroutineScope(SupervisorJob() + Dispatchers.Default),
) : DefaultLifecycleObserver {

  val mutablePurchases = MutableStateFlow(emptyList<Purchase>())
  val mutablePurchaseHistoryRecords = MutableStateFlow(emptyList<PurchaseHistoryRecord>())
  val mutableProductDetailsWithSubscriptionList = MutableStateFlow(emptyList<ProductDetails>())
  val mutableProductDetailsWithInAppItemList = MutableStateFlow(emptyList<ProductDetails>())

  override fun onCreate(owner: LifecycleOwner) {
    coroutineScope.launch {
      try {
        val connectionState = googleBillingService.connect()
        if (connectionState == ConnectionState.CONNECTED) {
          val productDetailsWithSubscriptionList =
            googleBillingService.queryProductDetails(Constants.SubscriptionList)
          val productDetailsWithInAppItemList =
            googleBillingService.queryProductDetails(Constants.InAppItemList)
          mutableProductDetailsWithSubscriptionList.value = productDetailsWithSubscriptionList
          mutableProductDetailsWithInAppItemList.value = productDetailsWithInAppItemList
        }
      } catch (e: GoogleBillingServiceException) {
        Log.e(TAG, "$e")
      }
    }
  }

  override fun onDestroy(owner: LifecycleOwner) {
    googleBillingService.disconnect()
  }

  suspend fun queryAllPurchases() {
    try {
      val subscriptionsPurchases = googleBillingService.queryPurchases(ProductType.Subscription())
      val consumablePurchases = googleBillingService.queryPurchases(ProductType.InApp())
      mutablePurchases.value = subscriptionsPurchases + consumablePurchases
    } catch (e: GoogleBillingServiceException) {
      Log.e(TAG, "$e")
    }
  }

  suspend fun queryPurchaseHistoryRecords() {
    try {
      val subscriptionPurchaseHistoryRecords =
        googleBillingService.queryPurchaseHistory(ProductType.Subscription())
      val consumablePurchaseHistoryRecords =
        googleBillingService.queryPurchaseHistory(ProductType.InApp())
      mutablePurchaseHistoryRecords.value =
        subscriptionPurchaseHistoryRecords + consumablePurchaseHistoryRecords
    } catch (e: GoogleBillingServiceException) {
      Log.e(TAG, "$e")
    }
  }

  suspend fun purchaseSubscription(
    productDetails: ProductDetails,
    offerToken: String,
    activity: Activity,
  ): List<Purchase>? {
    return try {
      val purchases = googleBillingService.queryPurchases(ProductType.Subscription())

      // Upgrade or downgrade when purchase another subscription while subscribing to a subscription.
      val oldPurchase = purchases
        .find { purchase ->
          !purchase.products.contains(productDetails.productId)
        }
      Log.d(TAG, "oldPurchase=$oldPurchase")
      val oldPurchaseToken = if (oldPurchase?.purchaseState == Purchase.PurchaseState.PURCHASED) {
        oldPurchase.purchaseToken
      } else {
        null
      }

      googleBillingService.purchaseSubscription(
        productDetails = productDetails,
        offerToken = offerToken,
        activityRef = WeakReference(activity),
        oldPurchaseToken = oldPurchaseToken,
      )
    } catch (e: GoogleBillingServiceException) {
      Log.e(TAG, "$e")
      null
    }
  }

  suspend fun acknowledge(
    purchaseToken: String,
  ) {
    try {
      googleBillingService.acknowledgePurchase(purchaseToken)
    } catch (e: GoogleBillingServiceException) {
      Log.e(TAG, "$e")
    }
  }

  suspend fun purchaseConsumeProduct(
    productDetails: ProductDetails,
    activity: Activity,
  ): List<Purchase>? {
    return try {
      googleBillingService.purchaseConsumableProduct(
        productDetails = productDetails,
        activityRef = WeakReference(activity),
      )
    } catch (e: GoogleBillingServiceException) {
      Log.e(TAG, "$e")
      null
    }
  }

  suspend fun consume(
    purchaseToken: String
  ) {
    try {
      googleBillingService.consumePurchase(purchaseToken)
    } catch (e: GoogleBillingServiceException) {
      Log.e(TAG, "$e")
    }
  }

  companion object {

    private val TAG = BillingClientLifecycle::class.java.simpleName

    @Volatile
    private var INSTANCE: BillingClientLifecycle? = null

    fun getInstance(googleBillingService: GoogleBillingService): BillingClientLifecycle =
      INSTANCE ?: synchronized(this) {
        INSTANCE ?: BillingClientLifecycle(googleBillingService).also {
          INSTANCE = it
        }
      }
  }
}
