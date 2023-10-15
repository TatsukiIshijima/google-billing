package com.tatsuki.inappbilling.ui.compose.purchasehistoryrecords

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tatsuki.inappbilling.R
import com.tatsuki.inappbilling.model.PurchaseHistoryRecordUiModel
import com.tatsuki.inappbilling.model.fake
import com.tatsuki.inappbilling.ui.compose.util.HeaderItem
import com.tatsuki.inappbilling.ui.compose.util.ItemBackground

@Composable
fun PurchaseHistoryRecordsScreen(
  modifier: Modifier = Modifier,
  purchaseHistoryRecords: List<PurchaseHistoryRecordUiModel>,
  queryPurchaseHistoryRecords: suspend () -> Unit = {},
) {
  LaunchedEffect(Unit) {
    queryPurchaseHistoryRecords()
  }

  LazyColumn(
    modifier = modifier
      .fillMaxWidth()
      .padding(12.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    item {
      HeaderItem(title = stringResource(id = R.string.purchase_history_records_title))
    }
    items(purchaseHistoryRecords.size) { purchaseHistoryRecordIndex ->
      val purchaseHistoryRecord = purchaseHistoryRecords[purchaseHistoryRecordIndex]
      PurchaseHistoryRecordItem(
        purchaseHistoryRecord = purchaseHistoryRecord,
      )
    }
  }
}

@Composable
private fun PurchaseHistoryRecordItem(
  modifier: Modifier = Modifier,
  purchaseHistoryRecord: PurchaseHistoryRecordUiModel,
) {
  ItemBackground(
    modifier = modifier,
  ) {
    Column(
      modifier = modifier.fillMaxWidth(),
    ) {
      Text(
        text = purchaseHistoryRecord.products.joinToString(separator = ","),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onPrimaryContainer
      )
      Text(
        text = purchaseHistoryRecord.quantity.toString(),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onPrimaryContainer
      )
      Text(
        text = purchaseHistoryRecord.purchaseTime.toString(),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onPrimaryContainer
      )
    }
  }
}

@Preview
@Composable
fun PreviewPurchaseHistoryRecordsScreen() {
  PurchaseHistoryRecordsScreen(
    purchaseHistoryRecords = listOf(
      PurchaseHistoryRecordUiModel.fake(),
      PurchaseHistoryRecordUiModel.fake(),
      PurchaseHistoryRecordUiModel.fake(),
    )
  )
}