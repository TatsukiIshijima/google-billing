package com.tatsuki.inappbilling.viewmodel

import androidx.lifecycle.ViewModel
import com.tatsuki.inappbilling.BillingClientLifecycle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val billingClientLifecycle: BillingClientLifecycle
) : ViewModel() {

  val purchases = billingClientLifecycle.mutablePurchases.asStateFlow()
  val purchaseHistoryRecords = billingClientLifecycle.mutablePurchaseHistoryRecords.asStateFlow()
  val productDetailsWithSubscriptionList =
    billingClientLifecycle.mutableProductDetailsWithSubscriptionList.asStateFlow()
  val productDetailsWithInAppItemList =
    billingClientLifecycle.mutableProductDetailsWithInAppItemList.asStateFlow()
}
