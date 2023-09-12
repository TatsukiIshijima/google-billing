package com.tatsuki.inappbilling

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.tatsuki.google.billing.ConnectionState
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
//          val purchases = googleBillingService.queryPurchases(ProductType.Subscription())
//          Log.i(TAG, "purchases=$purchases")
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
            )
          )
          Log.i(TAG, "productDetailsList=$productDetailsList")
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

  suspend fun purchase(
    productDetails: ProductDetails,
    offerToken: String,
    activity: Activity,
  ): List<Purchase>? {
    return googleBillingService.purchase(productDetails, offerToken, activity)
  }

  suspend fun acknowledge(
    purchaseToken: String
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
