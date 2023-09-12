package com.tatsuki.inappbilling

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import com.android.billingclient.api.ProductDetails
import com.tatsuki.google.billing.GoogleBillingServiceException
import com.tatsuki.inappbilling.model.ProductDetailsUiModel
import com.tatsuki.inappbilling.ui.compose.ProductDetailsListScreen
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
      val productDetails: List<ProductDetails> by mainViewModel.productDetailsList.collectAsState()

      InAppBillingTheme {
        Scaffold(
          topBar = {
            TopAppBar(
              colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary
              ),
              title = {
                Text(text = stringResource(id = R.string.app_name))
              }
            )
          },
        ) { innerPadding ->
          ProductDetailsListScreen(
            paddingValues = innerPadding,
            productDetailsList = productDetails.map { ProductDetailsUiModel.from(it) },
            onClick = { selectedProductDetailsIndex, selectedOfferToken ->
              composableScope.launch {
                try {
                  val selectedProductDetails = productDetails[selectedProductDetailsIndex]
                  billingClientLifecycle.purchase(
                    productDetails = selectedProductDetails,
                    offerToken = selectedOfferToken,
                    activity = this@MainActivity,
                  )?.let { purchases ->
                    val purchase = purchases.firstOrNull { !it.isAcknowledged } ?: return@let
                    billingClientLifecycle.acknowledge(purchase.purchaseToken)
                  }
                } catch (e: GoogleBillingServiceException) {
                  Log.e(TAG, "$e")
                }
              }
            }
          )
        }
      }
    }
  }

  companion object {
    val TAG = MainActivity::class.java.simpleName
  }
}
