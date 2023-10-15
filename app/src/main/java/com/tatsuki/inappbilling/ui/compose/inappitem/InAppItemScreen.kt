package com.tatsuki.inappbilling.ui.compose.inappitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tatsuki.inappbilling.R
import com.tatsuki.inappbilling.model.OneTimePurchaseOfferDetailUiModel
import com.tatsuki.inappbilling.model.ProductDetailsUiModel
import com.tatsuki.inappbilling.ui.compose.util.ItemBackground
import com.tatsuki.inappbilling.ui.compose.util.PropertyText
import com.tatsuki.inappbilling.ui.theme.InAppBillingTheme

@Composable
fun InAppItemScreen(
  modifier: Modifier = Modifier,
  productDetailsList: List<ProductDetailsUiModel>,
  onClick: (uiModel: OneTimePurchaseOfferDetailUiModel) -> Unit = { _ -> }
) {
  LazyColumn(
    modifier = modifier
      .fillMaxWidth()
      .padding(12.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    val oneTimePurchaseOfferDetailList = productDetailsList.mapNotNull {
      it.oneTimePurchaseOfferDetail
    }
    items(oneTimePurchaseOfferDetailList.size) { index ->
      val oneTimePurchaseOfferDetail = oneTimePurchaseOfferDetailList[index]
      OneTimeOfferDetailsItem(
        onClick = { onClick(oneTimePurchaseOfferDetail) },
        oneTimePurchaseOfferDetailUiModel = oneTimePurchaseOfferDetail
      )
    }
  }
}

@Preview
@Composable
private fun PreviewInAppItemScreen() {
  InAppBillingTheme {
    InAppItemScreen(
      productDetailsList = (0..2).mapIndexed { index, productDetails ->
        ProductDetailsUiModel.fake(
          index = index,
        )
      }
    )
  }
}

@Composable
private fun OneTimeOfferDetailsItem(
  modifier: Modifier = Modifier,
  onClick: (uiModel: OneTimePurchaseOfferDetailUiModel) -> Unit = {},
  oneTimePurchaseOfferDetailUiModel: OneTimePurchaseOfferDetailUiModel
) {
  ItemBackground(
    modifier = modifier,
    onClick = { onClick(oneTimePurchaseOfferDetailUiModel) }
  ) {
    Column(
      verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
      PropertyText(
        labelId = R.string.item_name_label,
        text = oneTimePurchaseOfferDetailUiModel.name
      )
      PropertyText(
        labelId = R.string.productId_label,
        text = oneTimePurchaseOfferDetailUiModel.productId
      )
      PropertyText(
        labelId = R.string.product_type_label,
        text = oneTimePurchaseOfferDetailUiModel.productType
      )
      PropertyText(
        labelId = R.string.price_label,
        text = oneTimePurchaseOfferDetailUiModel.pricingPhase.formattedPrice
      )
    }
  }
}

@Preview
@Composable
private fun PreviewOneTimeOfferDetailsItem() {
  InAppBillingTheme {
    OneTimeOfferDetailsItem(
      oneTimePurchaseOfferDetailUiModel = OneTimePurchaseOfferDetailUiModel.fake()
    )
  }
}