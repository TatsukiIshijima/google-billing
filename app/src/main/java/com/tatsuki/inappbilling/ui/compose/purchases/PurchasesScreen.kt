package com.tatsuki.inappbilling.ui.compose.purchases

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tatsuki.billing.feature.model.type.PurchaseState
import com.tatsuki.inappbilling.model.PurchaseUiModel
import com.tatsuki.inappbilling.model.fake
import com.tatsuki.inappbilling.ui.compose.util.ItemBackground
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
    ) {
      Text(
        text = purchase.products.joinToString(separator = ","),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onPrimaryContainer
      )
      Text(
        text = purchase.purchaseState.toName(),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onPrimaryContainer
      )
      Text(
        text = purchase.quantity.toString(),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onPrimaryContainer
      )
      Text(
        text = purchase.purchaseTime.toString(),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onPrimaryContainer
      )
      Text(
        text = purchase.orderId ?: "-",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onPrimaryContainer
      )
      Text(
        text = purchase.isAcknowledged.toString(),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onPrimaryContainer
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