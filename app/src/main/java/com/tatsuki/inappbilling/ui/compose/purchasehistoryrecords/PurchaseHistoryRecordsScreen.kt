package com.tatsuki.inappbilling.ui.compose.purchasehistoryrecords

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
import com.tatsuki.inappbilling.R
import com.tatsuki.inappbilling.extension.toDateString
import com.tatsuki.inappbilling.model.PurchaseHistoryRecordUiModel
import com.tatsuki.inappbilling.model.fake
import com.tatsuki.inappbilling.ui.compose.util.HeaderItem
import com.tatsuki.inappbilling.ui.compose.util.ItemBackground
import com.tatsuki.inappbilling.ui.compose.util.PropertyText

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
      verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
      PropertyText(
        labelId = R.string.productIds_label,
        text = purchaseHistoryRecord.products.joinToString(separator = ",")
      )
      PropertyText(
        labelId = R.string.quantity_label,
        text = purchaseHistoryRecord.quantity.toString()
      )
      PropertyText(
        labelId = R.string.purchase_time_label,
        text = purchaseHistoryRecord.purchaseTime.toDateString()
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