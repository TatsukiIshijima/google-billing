package com.tatsuki.googlebillingsample

import android.app.Activity
import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.tatsuki.google.billing.GoogleBillingClientImpl
import com.tatsuki.google.billing.GoogleBillingService
import com.tatsuki.google.billing.GoogleBillingServiceImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * https://github.com/android/play-billing-samples/blob/main/ClassyTaxiAppKotlin/app/src/main/java/com/example/billing/gpbl/BillingClientLifecycle.kt
 */
class BillingClientLifecycle(
  private val applicationContext: Context,
  private val coroutineScope: CoroutineScope =
    CoroutineScope(SupervisorJob() + Dispatchers.Default),
) : DefaultLifecycleObserver {

  private lateinit var billingService: GoogleBillingService

  override fun onCreate(owner: LifecycleOwner) {

    val billingClient = BillingClient.newBuilder(applicationContext).build()
    val googleBillingClientImpl = GoogleBillingClientImpl(billingClient)
    billingService = GoogleBillingServiceImpl(googleBillingClientImpl)

    coroutineScope.launch {
      billingService.connect()
    }
  }

  override fun onDestroy(owner: LifecycleOwner) {
    billingService.disconnect()
  }

  suspend fun purchase(
    productDetails: ProductDetails,
    offerToken: String?,
    activity: Activity,
  ): List<Purchase>? {
    return billingService.purchase(productDetails, offerToken, activity)
  }

  companion object {
    @Volatile
    private var INSTANCE: BillingClientLifecycle? = null

    fun getInstance(applicationContext: Context): BillingClientLifecycle =
      INSTANCE ?: synchronized(this) {
        INSTANCE ?: BillingClientLifecycle(applicationContext).also { INSTANCE = it }
      }
  }
}
