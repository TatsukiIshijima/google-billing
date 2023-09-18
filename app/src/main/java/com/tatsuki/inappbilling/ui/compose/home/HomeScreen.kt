package com.tatsuki.inappbilling.ui.compose.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tatsuki.inappbilling.R
import com.tatsuki.inappbilling.model.OneTimePurchaseOfferDetailUiModel
import com.tatsuki.inappbilling.model.ProductDetailsUiModel
import com.tatsuki.inappbilling.model.SubscriptionOfferDetailUiModel
import com.tatsuki.inappbilling.ui.compose.inappitem.InAppItemScreen
import com.tatsuki.inappbilling.ui.compose.subscription.SubscriptionScreen
import com.tatsuki.inappbilling.ui.theme.InAppBillingTheme

enum class InAppBillingPage(
  val index: Int,
  @StringRes val titleResId: Int,
) {
  SUBSCRIPTION(0, R.string.subscription_title),
  IN_APP_ITEM(1, R.string.in_app_item_title)
  ;

  companion object {
    fun from(index: Int) = InAppBillingPage.values().first { it.index == index }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  modifier: Modifier = Modifier,
  productDetailsWithSubscriptionList: List<ProductDetailsUiModel>,
  productDetailsWithInAppItemList: List<ProductDetailsUiModel>,
  onSubscriptionClick: (uiModel: SubscriptionOfferDetailUiModel) -> Unit = { _ -> },
  onInAppItemClick: (uiModel: OneTimePurchaseOfferDetailUiModel) -> Unit = { _ -> },
) {
  var selectedTabIndex by remember { mutableStateOf(0) }
  Scaffold(
    modifier = modifier,
    topBar = {
      HomeTopAppBar()
    }
  ) { innerPadding ->
    HomeTabScreen(
      modifier = Modifier
        .fillMaxWidth()
        .padding(innerPadding),
      selectedTabIndex = selectedTabIndex,
      onTabClick = { inAppBillingPage ->
        selectedTabIndex = when (inAppBillingPage) {
          InAppBillingPage.SUBSCRIPTION -> 0
          InAppBillingPage.IN_APP_ITEM -> 1
        }
      },
      productDetailsWithSubscriptionList = productDetailsWithSubscriptionList,
      productDetailsWithInAppItemList = productDetailsWithInAppItemList,
      onSubscriptionClick = { uiModel ->
        onSubscriptionClick(uiModel)
      },
      onInAppItemClick = { uiModel ->
        onInAppItemClick(uiModel)
      },
    )
  }
}

@Preview
@Composable
private fun PreviewHomeScreen() {
  InAppBillingTheme {
    HomeScreen(
      productDetailsWithSubscriptionList = (0..1).mapIndexed { index, productDetails ->
        ProductDetailsUiModel.fake(
          index = index,
        )
      },
      productDetailsWithInAppItemList = (0..1).mapIndexed { index, productDetails ->
        ProductDetailsUiModel.fake(
          index = index,
        )
      },
      onSubscriptionClick = {},
      onInAppItemClick = {},
    )
  }
}

@Composable
private fun HomeTabScreen(
  modifier: Modifier = Modifier,
  selectedTabIndex: Int,
  onTabClick: (InAppBillingPage) -> Unit,
  tabs: Array<InAppBillingPage> = InAppBillingPage.values(),
  productDetailsWithSubscriptionList: List<ProductDetailsUiModel>,
  productDetailsWithInAppItemList: List<ProductDetailsUiModel>,
  onSubscriptionClick: (uiModel: SubscriptionOfferDetailUiModel) -> Unit = { _ -> },
  onInAppItemClick: (uiModel: OneTimePurchaseOfferDetailUiModel) -> Unit = { _ -> },
) {
  Column(modifier) {
    TabRow(
      selectedTabIndex = selectedTabIndex,
    ) {
      tabs.forEachIndexed { index, inAppBillingPage ->
        val title = stringResource(id = inAppBillingPage.titleResId)
        Tab(
          selected = selectedTabIndex == index,
          onClick = { onTabClick(inAppBillingPage) },
          text = { Text(text = title) }
        )
      }
    }
    when (InAppBillingPage.from(selectedTabIndex)) {
      InAppBillingPage.SUBSCRIPTION -> SubscriptionScreen(
        productDetailsList = productDetailsWithSubscriptionList,
        onClick = { uiModel -> onSubscriptionClick(uiModel) }
      )

      InAppBillingPage.IN_APP_ITEM -> InAppItemScreen(
        productDetailsList = productDetailsWithInAppItemList,
        onClick = { uiModel ->
          onInAppItemClick(uiModel)
        }
      )
    }
  }
}

@Preview
@Composable
private fun PreviewHomeTabScreen() {
  InAppBillingTheme {
    HomeTabScreen(
      productDetailsWithSubscriptionList = emptyList(),
      productDetailsWithInAppItemList = emptyList(),
      selectedTabIndex = 0,
      onTabClick = {}
    )
  }
}