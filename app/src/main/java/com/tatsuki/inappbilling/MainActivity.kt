package com.tatsuki.inappbilling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.tatsuki.inappbilling.ui.compose.InAppBillingApp
import com.tatsuki.inappbilling.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  @Inject
  lateinit var billingClientLifecycle: BillingClientLifecycle

  private val mainViewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    lifecycle.addObserver(billingClientLifecycle)

    setContent {
      InAppBillingApp(
        mainViewModel = mainViewModel,
        billingClientLifecycle = billingClientLifecycle,
      )
    }
  }
}
