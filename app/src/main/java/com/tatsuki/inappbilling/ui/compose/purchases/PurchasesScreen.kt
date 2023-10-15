package com.tatsuki.inappbilling.ui.compose.purchases

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tatsuki.billing.feature.model.type.PurchaseState
import com.tatsuki.inappbilling.R
import com.tatsuki.inappbilling.model.PurchaseUiModel
import com.tatsuki.inappbilling.model.fake
import com.tatsuki.inappbilling.ui.compose.util.HeaderItem
import com.tatsuki.inappbilling.ui.compose.util.ItemBackground
import com.tatsuki.inappbilling.ui.compose.util.PropertyText
import com.tatsuki.inappbilling.ui.theme.InAppBillingTheme

@Composable
fun PurchasesScreen(
  modifier: Modifier = Modifier,
  purchases: List<PurchaseUiModel>,
  queryPurchases: suspend () -> Unit = {},
) {
  LaunchedEffect(Unit) {
    queryPurchases()
  }

  LazyColumn(
    modifier = modifier
      .fillMaxWidth()
      .padding(12.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    item {
      HeaderItem(title = stringResource(id = R.string.purchases_title))
    }
    items(purchases.size) { purchaseIndex ->
      val purchase = purchases[purchaseIndex]
      PurchaseItem(
        purchase = purchase,
      )
    }
  }
}

@Composable
private fun PurchaseItem(
  modifier: Modifier = Modifier,
  purchase: PurchaseUiModel,
) {
  ItemBackground(
    modifier = modifier,
  ) {
    Column(
      modifier = modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      PropertyText(
        labelId = R.string.productIds_label,
        text = purchase.products.joinToString(separator = ",")
      )
      PropertyText(
        labelId = R.string.purchase_state_label,
        text = purchase.purchaseState.toName()
      )
      PropertyText(
        labelId = R.string.quantity_label,
        text = purchase.quantity.toString()
      )
      PropertyText(
        labelId = R.string.purchase_time_label,
        text = purchase.purchaseTime.toString()
      )
      PropertyText(
        labelId = R.string.order_id_label,
        text = purchase.orderId ?: "-"
      )
      PropertyText(
        labelId = R.string.is_acknowledged_label,
        text = purchase.isAcknowledged.toString()
      )
    }
  }
}

private fun PurchaseState.toName(): String {
  return when (this) {
    is PurchaseState.UnspecifiedState -> "UnspecifiedState"
    is PurchaseState.Purchased -> "Purchased"
    is PurchaseState.Pending -> "Pending"
  }
}

@Preview
@Composable
private fun PreviewPurchasesScreen() {
  InAppBillingTheme {
    PurchasesScreen(
      purchases = listOf(
        PurchaseUiModel.fake(),
        PurchaseUiModel.fake(),
        PurchaseUiModel.fake(),
      )
    )
  }
}