package com.tatsuki.inappbilling

import android.app.Activity
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.tatsuki.google.billing.model.type.ConnectionState
import com.tatsuki.google.billing.GoogleBillingService
import com.tatsuki.google.billing.GoogleBillingServiceException
import com.tatsuki.google.billing.model.Product
import com.tatsuki.google.billing.model.ProductId
import com.tatsuki.google.billing.model.ProductType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
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

  val mutableProductDetailsList = MutableStateFlow(emptyList<ProductDetails>())

  override fun onCreate(owner: LifecycleOwner) {
    coroutineScope.launch {
      try {
        val connectionState = googleBillingService.connect()
        if (connectionState == ConnectionState.CONNECTED) {
          val productDetailsList = googleBillingService.queryProductDetails(
            listOf(
              Product(
                ProductId(Constants.TEST_IN_APP_BILLING_SUBSCRIPTION_PLAN_1),
                ProductType.Subscription()
              ),
              Product(
                ProductId(Constants.TEST_IN_APP_BILLING_SUBSCRIPTION_PLAN_2),
                ProductType.Subscription()
              ),
              Product(
                ProductId(Constants.TEST_IN_APP_BILLING_SUBSCRIPTION_PLAN_3),
                ProductType.Subscription()
              ),
              Product(
                ProductId(Constants.TEST_IN_APP_BILLING_SUBSCRIPTION_PLAN_4),
                ProductType.Subscription()
              ),
              Product(
                ProductId(Constants.TEST_IN_APP_BILLING_SUBSCRIPTION_PLAN_5),
                ProductType.Subscription()
              )
            )
          )
          mutableProductDetailsList.value = productDetailsList
        }
      } catch (e: GoogleBillingServiceException) {
        Log.e(TAG, "$e")
      }
    }
  }

  override fun onDestroy(owner: LifecycleOwner) {
    googleBillingService.disconnect()
  }

  suspend fun purchaseSubscription(
    productDetails: ProductDetails,
    offerToken: String,
    activity: Activity,
  ): List<Purchase>? {

    Log.i(TAG, "selectedProductDetails=$productDetails")
    val purchases = googleBillingService.queryPurchases(ProductType.Subscription())

    // Upgrade or downgrade when purchase another subscription while subscribing to a subscription.
    val oldPurchase = purchases
      .find { purchase ->
        !purchase.products.contains(productDetails.productId)
      }
    Log.i(TAG, "oldPurchase=$oldPurchase")
    val oldPurchaseToken = if (oldPurchase?.purchaseState == Purchase.PurchaseState.PURCHASED) {
      oldPurchase.purchaseToken
    } else {
      null
    }

    return googleBillingService.purchaseSubscription(
      productDetails = productDetails,
      offerToken = offerToken,
      activity = activity,
      oldPurchaseToken = oldPurchaseToken,
    )
  }

  suspend fun acknowledge(
    purchaseToken: String,
  ) {
    googleBillingService.acknowledgePurchase(purchaseToken)
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
