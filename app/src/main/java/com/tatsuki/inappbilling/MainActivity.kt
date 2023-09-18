package com.tatsuki.inappbilling

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.tatsuki.inappbilling.model.ProductDetailsUiModel
import com.tatsuki.inappbilling.ui.compose.home.HomeScreen
import com.tatsuki.inappbilling.ui.theme.InAppBillingTheme
import com.tatsuki.inappbilling.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  @Inject
  lateinit var billingClientLifecycle: BillingClientLifecycle

  private val mainViewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    lifecycle.addObserver(billingClientLifecycle)

    setContent {
      val composableScope = rememberCoroutineScope()
      val productDetailsWithSubscriptionList: List<ProductDetails> by mainViewModel.productDetailsWithSubscriptionList.collectAsState()
      val productDetailsWithInAppItemList: List<ProductDetails> by mainViewModel.productDetailsWithInAppItemList.collectAsState()

      InAppBillingTheme {
        HomeScreen(
          productDetailsWithSubscriptionList = productDetailsWithSubscriptionList.mapIndexed { index, productDetails ->
            ProductDetailsUiModel.from(
              index = index,
              productDetails = productDetails
            )
          },
          productDetailsWithInAppItemList = productDetailsWithInAppItemList.mapIndexed { index, productDetails ->
            ProductDetailsUiModel.from(
              index = index,
              productDetails = productDetails,
            )
          },
          onSubscriptionClick = { uiModel ->
            composableScope.launch {
              val selectedProductDetails = productDetailsWithSubscriptionList[uiModel.parentIndex]
              val selectedSubscriptionOfferDetail = selectedProductDetails.subscriptionOfferDetails?.get(uiModel.index)
              val selectedOfferToken = selectedSubscriptionOfferDetail?.offerToken ?: ""
              Log.d(TAG, "selectedProductDetails=$selectedProductDetails")
              billingClientLifecycle.purchaseSubscription(
                productDetails = selectedProductDetails,
                offerToken = selectedOfferToken,
                activity = this@MainActivity,
              )?.let { purchases ->
                val purchase = purchases.find { purchase ->
                  purchase.products.contains(selectedProductDetails.productId) &&
                          purchase.purchaseState == Purchase.PurchaseState.PURCHASED &&
                          !purchase.isAcknowledged
                } ?: return@let
                billingClientLifecycle.acknowledge(purchase.purchaseToken)
              }
            }
          },
          onInAppItemClick = { uiModel ->
            composableScope.launch {
              val selectedProductDetails = productDetailsWithInAppItemList[uiModel.index]
              Log.d(TAG, "selectedProductDetails=$selectedProductDetails")
              billingClientLifecycle.purchaseConsumeProduct(
                productDetails = selectedProductDetails,
                activity = this@MainActivity
              )?.let { purchases ->
                val purchase = purchases.first()
                if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) {
                  return@let
                }
                billingClientLifecycle.consume(purchase.purchaseToken)
              }
            }
          },
        )
      }
    }
  }

  companion object {
    val TAG = MainActivity::class.java.simpleName
  }
}
