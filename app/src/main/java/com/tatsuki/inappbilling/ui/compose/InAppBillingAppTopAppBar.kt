package com.tatsuki.inappbilling.ui.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tatsuki.inappbilling.R
import com.tatsuki.inappbilling.ui.theme.InAppBillingTheme

@ExperimentalMaterial3Api
@Composable
fun InAppBillingAppTopAppBar(
  onMenuClick: () -> Unit = { }
) {
  TopAppBar(
    colors = TopAppBarDefaults.smallTopAppBarColors(
      containerColor = MaterialTheme.colorScheme.primary,
      titleContentColor = MaterialTheme.colorScheme.onPrimary
    ),
    title = {
      Text(text = stringResource(id = R.string.app_name))
    },
    navigationIcon = {
      IconButton(
        onClick = { onMenuClick() }
      ) {
        Icon(
          imageVector = Icons.Filled.Menu,
          contentDescription = "Menu",
          tint = MaterialTheme.colorScheme.onPrimary,
        )
      }
    },
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PreviewHomeTopAppBar() {
  InAppBillingTheme {
    InAppBillingAppTopAppBar()
  }
}