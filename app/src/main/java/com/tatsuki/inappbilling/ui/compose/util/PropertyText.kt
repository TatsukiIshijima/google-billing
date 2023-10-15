package com.tatsuki.inappbilling.ui.compose.util

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tatsuki.inappbilling.R
import com.tatsuki.inappbilling.ui.theme.InAppBillingTheme

@Composable
fun PropertyText(
  modifier: Modifier = Modifier,
  @StringRes labelId: Int,
  text: String,
) {
  Column(
    modifier = modifier.fillMaxWidth()
  ) {
    Text(
      text = stringResource(id = labelId),
      color = MaterialTheme.colorScheme.onPrimaryContainer,
      style = MaterialTheme.typography.titleMedium,
    )
    Text(
      text = text,
      color = MaterialTheme.colorScheme.onPrimaryContainer,
      style = MaterialTheme.typography.bodyMedium,
    )
  }
}

@Preview
@Composable
fun PreviewPropertyText() {
  InAppBillingTheme {
    PropertyText(
      labelId = R.string.productId_label,
      text = "productId"
    )
  }
}