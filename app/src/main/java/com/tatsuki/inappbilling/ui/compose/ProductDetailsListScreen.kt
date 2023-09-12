package com.tatsuki.inappbilling.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tatsuki.inappbilling.model.PricingPhaseUiModel
import com.tatsuki.inappbilling.model.ProductDetailsUiModel
import com.tatsuki.inappbilling.model.SubscriptionOfferDetailUiModel
import com.tatsuki.inappbilling.ui.theme.InAppBillingTheme

@Composable
fun ProductDetailsListScreen(
  modifier: Modifier = Modifier,
  paddingValues: PaddingValues,
  productDetailsList: List<ProductDetailsUiModel>,
  onClick: (index: Int, offerToken: String) -> Unit = { _, _ -> },
) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .padding(paddingValues)
  ) {
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .padding(12.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      items(productDetailsList.size) { productDetailsIndex ->
        val productDetails = productDetailsList[productDetailsIndex]
        ProductDetailsItem(
          header = {
            ProductDetailsItemHeader(
              modifier = Modifier,
              productName = productDetails.name,
            )
          },
          body = {
            ProductDetailsItemBody(
              productDetails = productDetails,
              onClick = { subscriptionOfferDetailsIndex ->
                val offerToken = productDetails.subscriptionOfferDetailsList[subscriptionOfferDetailsIndex].offerToken
                onClick(productDetailsIndex, offerToken)
              }
            )
          },
        )
      }
    }
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
    Spacer(modifier = Modifier.size(8.dp))
    body()
  }
}

@Composable
private fun ProductDetailsItemHeader(
  modifier: Modifier = Modifier,
  productName: String,
) {
  Text(
    modifier = modifier,
    text = productName,
    fontSize = MaterialTheme.typography.titleLarge.fontSize,
    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
    color = MaterialTheme.colorScheme.onPrimaryContainer
  )
}

@Composable
private fun ProductDetailsItemBody(
  productDetails: ProductDetailsUiModel,
  onClick: (index: Int) -> Unit = {},
) {
  productDetails.subscriptionOfferDetailsList.forEachIndexed { index, subscriptionOfferDetail ->
    Column {
      Spacer(modifier = Modifier.size(6.dp))
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(16.dp)
          )
          .clickable { onClick(index) }
          .padding(16.dp),
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
        ) {
          Text(
            text = productDetails.productId,
            color = MaterialTheme.colorScheme.onPrimaryContainer
          )
          Text(
            text = productDetails.productType,
            color = MaterialTheme.colorScheme.onPrimaryContainer
          )
          Text(
            text = subscriptionOfferDetail.basePlanId,
            color = MaterialTheme.colorScheme.onPrimaryContainer
          )
          Text(
            text = subscriptionOfferDetail.offerId,
            color = MaterialTheme.colorScheme.onPrimaryContainer
          )
          Text(
            text = subscriptionOfferDetail.pricingPhase.formattedPrice,
            color = MaterialTheme.colorScheme.onPrimaryContainer
          )
        }
      }
      Spacer(modifier = Modifier.size(6.dp))
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PreviewProductListScreen() {
  InAppBillingTheme {
    Scaffold(
      topBar = {
        InAppBillingTopAppBar()
      }
    ) { innerPadding ->
      ProductDetailsListScreen(
        paddingValues = innerPadding,
        productDetailsList = listOf(
          ProductDetailsUiModel(
            name = "name",
            productId = "productId",
            productType = "productType",
            subscriptionOfferDetailsList = listOf(
              SubscriptionOfferDetailUiModel(
                basePlanId = "basePlanId",
                offerId = "offerId",
                offerToken = "offerToken",
                pricingPhase = PricingPhaseUiModel(
                  formattedPrice = "formattedPrice",
                )
              ),
              SubscriptionOfferDetailUiModel(
                basePlanId = "basePlanId",
                offerId = "offerId",
                offerToken = "offerToken",
                pricingPhase = PricingPhaseUiModel(
                  formattedPrice = "formattedPrice",
                )
              )
            )
          ),
          ProductDetailsUiModel(
            name = "name",
            productId = "productId",
            productType = "productType",
            subscriptionOfferDetailsList = listOf(
              SubscriptionOfferDetailUiModel(
                basePlanId = "basePlanId",
                offerId = "offerId",
                offerToken = "offerToken",
                pricingPhase = PricingPhaseUiModel(
                  formattedPrice = "formattedPrice",
                )
              )
            )
          )
        )
      )
    }
  }
}
