package com.tatsuki.inappbilling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tatsuki.inappbilling.ui.theme.InAppBillingTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  @Inject
  lateinit var billingClientLifecycle: BillingClientLifecycle

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    lifecycle.addObserver(billingClientLifecycle)

    setContent {
      InAppBillingTheme {
        Scaffold(
          topBar = {
            TopAppBar(
              colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary
              ),
              title = {
                Text(text = stringResource(id = R.string.app_name))
              }
            )
          },
        ) { innerPadding ->
          MainScreen(innerPadding)
        }
      }
    }
  }
}

@ExperimentalMaterial3Api
@Composable
private fun InAppBillingTopAppBar() {
  TopAppBar(
    colors = TopAppBarDefaults.smallTopAppBarColors(
      containerColor = MaterialTheme.colorScheme.primaryContainer,
      titleContentColor = MaterialTheme.colorScheme.primary
    ),
    title = {
      Text(text = stringResource(id = R.string.app_name))
    }
  )
}

@Composable
private fun MainScreen(paddingValues: PaddingValues) {
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PreviewMainScreen() {
  InAppBillingTheme {
    Scaffold(
      topBar = {
        InAppBillingTopAppBar()
      }
    ) { innerPadding ->
      MainScreen(paddingValues = innerPadding)
    }
  }
}
