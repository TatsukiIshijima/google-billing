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
import com.tatsuki.google.billing.model.ProductType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * https://github.com/android/play-billing-samples/blob/main/ClassyTaxiAppKotlin/app/src/main/java/com/example/billing/gpbl/BillingClientLifecycle.kt
 */

@Singleton
class BillingClientLifecycle @Inject constructor(
  @ApplicationContext val applicationContext: Context,
  private val googleBillingService: GoogleBillingService,
  private val coroutineScope: CoroutineScope =
    CoroutineScope(SupervisorJob() + Dispatchers.Default),
) : DefaultLifecycleObserver {

  override fun onCreate(owner: LifecycleOwner) {
    coroutineScope.launch {
      val connectionState = googleBillingService.connect()
      if (connectionState == ConnectionState.CONNECTED) {
        val purchases = googleBillingService.queryPurchases(ProductType.Subscription())
        Log.i(TAG, "purchases=$purchases")
      }
    }
  }

  override fun onDestroy(owner: LifecycleOwner) {
    googleBillingService.disconnect()
  }

  suspend fun purchase(
    productDetails: ProductDetails,
    offerToken: String?,
    activity: Activity,
  ): List<Purchase>? {
    return googleBillingService.purchase(productDetails, offerToken, activity)
  }

  companion object {

    private val TAG = BillingClientLifecycle::class.java.simpleName

    @Volatile
    private var INSTANCE: BillingClientLifecycle? = null

    fun getInstance(
      applicationContext: Context,
      googleBillingService: GoogleBillingService,
    ): BillingClientLifecycle =
      INSTANCE ?: synchronized(this) {
        INSTANCE ?: BillingClientLifecycle(
          applicationContext,
          googleBillingService,
        ).also {
          INSTANCE = it
        }
      }
  }
}
