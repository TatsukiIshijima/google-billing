package com.tatsuki.inappbilling.ui.compose.subscription

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tatsuki.inappbilling.R
import com.tatsuki.inappbilling.model.ProductDetailsUiModel
import com.tatsuki.inappbilling.model.SubscriptionOfferDetailUiModel
import com.tatsuki.inappbilling.ui.compose.util.HeaderItem
import com.tatsuki.inappbilling.ui.compose.util.ItemBackground
import com.tatsuki.inappbilling.ui.compose.util.PropertyText
import com.tatsuki.inappbilling.ui.theme.InAppBillingTheme

@Composable
fun SubscriptionScreen(
  modifier: Modifier = Modifier,
  productDetailsList: List<ProductDetailsUiModel>,
  onClick: (uiModel: SubscriptionOfferDetailUiModel) -> Unit = { _ -> },
) {
  LazyColumn(
    modifier = modifier
      .fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(6.dp),
  ) {
    items(productDetailsList.size) { productDetailsIndex ->
      val productDetails = productDetailsList[productDetailsIndex]
      ProductDetailsItem(
        modifier = Modifier.padding(12.dp),
        header = {
          HeaderItem(
            modifier = Modifier,
            title = productDetails.name,
          )
        },
        body = {
          SubscriptionOfferDetailsItemBody(
            productDetails = productDetails,
            onClick = { subscriptionOfferDetailsIndex ->
              onClick(productDetails.subscriptionOfferDetailsList[subscriptionOfferDetailsIndex])
            }
          )
        },
      )
    }
  }
}

@Preview
@Composable
private fun PreviewSubscriptionScreen() {
  InAppBillingTheme {
    SubscriptionScreen(
      productDetailsList = (0..2).map {
        ProductDetailsUiModel.fake()
      }
    )
  }
}

@Composable
private fun ProductDetailsItem(
  modifier: Modifier = Modifier,
  header: @Composable () -> Unit,
  body: @Composable () -> Unit,
) {
  Column(
    modifier = modifier
  ) {
    header()
    Spacer(modifier = Modifier.size(6.dp))
    body()
  }
}

@Composable
private fun SubscriptionOfferDetailsItemBody(
  productDetails: ProductDetailsUiModel,
  onClick: (index: Int) -> Unit = {},
) {
  productDetails.subscriptionOfferDetailsList.forEachIndexed { index, subscriptionOfferDetail ->
    Column {
      Spacer(modifier = Modifier.size(6.dp))
      ItemBackground(
        onClick = { onClick(index) }
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          PropertyText(
            labelId = R.string.productId_label,
            text = productDetails.productId,
          )
          PropertyText(
            labelId = R.string.product_type_label,
            text = productDetails.productType
          )
          PropertyText(
            labelId = R.string.base_plan_id_label,
            text = subscriptionOfferDetail.basePlanId,
          )
          PropertyText(
            labelId = R.string.offer_id_label,
            text = subscriptionOfferDetail.offerId
          )
          PropertyText(
            labelId = R.string.price_label,
            text = subscriptionOfferDetail.pricingPhase.formattedPrice
          )
        }
      }
      Spacer(modifier = Modifier.size(6.dp))
    }
  }
}

@Preview
@Composable
private fun PreviewSubscriptionOfferDetailsItemBody() {
  InAppBillingTheme {
    SubscriptionOfferDetailsItemBody(productDetails = ProductDetailsUiModel.fake())
  }
}
