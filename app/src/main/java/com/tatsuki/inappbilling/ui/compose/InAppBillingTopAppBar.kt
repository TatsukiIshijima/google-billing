package com.tatsuki.inappbilling.ui.compose

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tatsuki.inappbilling.R

@ExperimentalMaterial3Api
@Composable
fun InAppBillingTopAppBar() {
  TopAppBar(
    colors = TopAppBarDefaults.smallTopAppBarColors(
      containerColor = MaterialTheme.colorScheme.primary,
      titleContentColor = MaterialTheme.colorScheme.onPrimary
    ),
    title = {
      Text(text = stringResource(id = R.string.app_name))
    }
  )
}
