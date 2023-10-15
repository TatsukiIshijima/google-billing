package com.tatsuki.inappbilling.ui.compose

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.tatsuki.inappbilling.BillingClientLifecycle
import com.tatsuki.inappbilling.model.ProductDetailsUiModel
import com.tatsuki.inappbilling.model.PurchaseUiModel
import com.tatsuki.inappbilling.ui.compose.home.HomeScreen
import com.tatsuki.inappbilling.ui.compose.purchases.PurchasesScreen
import com.tatsuki.inappbilling.ui.theme.InAppBillingTheme
import com.tatsuki.inappbilling.viewmodel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InAppBillingApp(
  modifier: Modifier = Modifier,
  mainViewModel: MainViewModel,
  billingClientLifecycle: BillingClientLifecycle,
) {
  InAppBillingTheme {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
      drawerState = drawerState,
      drawerContent = {
        InAppBillingAppMenuDrawer(onClickMenuItem = { menuItem ->
          coroutineScope.launch {
            navController.navigate(menuItem.toScreen().route)
            drawerState.close()
          }
        })
      },
      content = {
        Scaffold(
          modifier = modifier,
          topBar = {
            InAppBillingAppTopAppBar(
              onMenuClick = {
                coroutineScope.launch {
                  drawerState.open()
                }
              }
            )
          },
        ) { innerPadding ->
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
          ) {
            InAppBillingAppNavigation(
              navHostController = navController,
              mainViewModel = mainViewModel,
              billingClientLifecycle = billingClientLifecycle,
            )
          }
        }
      }
    )
  }
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
private fun InAppBillingAppNavigation(
  navHostController: NavHostController,
  mainViewModel: MainViewModel,
  billingClientLifecycle: BillingClientLifecycle,
) {
  NavHost(
    navController = navHostController,
    startDestination = Screen.Home.route
  ) {
    composable(Screen.Home.route) {

      val composableScope = rememberCoroutineScope()
      val activity = (LocalContext.current as Activity)
      val productDetailsWithSubscriptionList: List<ProductDetails> by mainViewModel.productDetailsWithSubscriptionList.collectAsState()
      val productDetailsWithInAppItemList: List<ProductDetails> by mainViewModel.productDetailsWithInAppItemList.collectAsState()

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
            val selectedSubscriptionOfferDetail =
              selectedProductDetails.subscriptionOfferDetails?.get(uiModel.index)
            val selectedOfferToken = selectedSubscriptionOfferDetail?.offerToken ?: ""
            Log.d("InAppBillingApp", "selectedProductDetails=$selectedProductDetails")
            billingClientLifecycle.purchaseSubscription(
              productDetails = selectedProductDetails,
              offerToken = selectedOfferToken,
              activity = activity,
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
            Log.d("InAppBillingApp", "selectedProductDetails=$selectedProductDetails")
            billingClientLifecycle.purchaseConsumeProduct(
              productDetails = selectedProductDetails,
              activity = activity
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
    composable(Screen.Purchases.route) {
      val purchases: List<Purchase> by mainViewModel.purchasesMutableList.collectAsState()
      PurchasesScreen(
        purchases = purchases.map { purchase ->
          PurchaseUiModel.from(purchase)
        },
        queryPurchases = {
          billingClientLifecycle.queryAllPurchases()
        }
      )
    }
    composable(Screen.PurchaseHistoryRecords.route) {

    }
  }
}

sealed class Screen(val route: String) {
  object Home : Screen("home")
  object Purchases : Screen("purchases")
  object PurchaseHistoryRecords : Screen("purchase_history_records")
}