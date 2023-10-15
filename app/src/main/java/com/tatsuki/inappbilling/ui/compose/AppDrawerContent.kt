package com.tatsuki.inappbilling.ui.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tatsuki.inappbilling.R
import com.tatsuki.inappbilling.ui.theme.InAppBillingTheme

enum class MenuItem(
  val index: Int,
  @StringRes val titleResId: Int,
) {
  PURCHASES(0, R.string.menu_item_purchases),
  PURCHASE_HISTORY_RECORDS(1, R.string.menu_item_purchase_history_records)
  ;
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawerContent(
  onClickMenuItem: (menuItem: MenuItem) -> Unit,
) {
  ModalDrawerSheet() {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colorScheme.primary)
    ) {
      Text(
        text = stringResource(id = R.string.app_menu),
        modifier = Modifier.padding(16.dp),
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.headlineSmall,
      )
    }
    MenuItem.values().forEach {
      Divider()
      NavigationDrawerItem(
        label = {
          Text(
            text = stringResource(id = it.titleResId),
            style = MaterialTheme.typography.titleMedium,
          )
        },
        selected = false,
        onClick = { onClickMenuItem(it) }
      )
    }
  }
}

@Preview
@Composable
private fun PreviewDrawerMenu() {
  InAppBillingTheme {
    AppDrawerContent(
      onClickMenuItem = {}
    )
  }
}