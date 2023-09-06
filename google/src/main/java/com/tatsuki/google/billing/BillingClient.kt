package com.tatsuki.google.billing

import android.util.Log

class BillingClient {

  fun purchase() {
    Log.d(TAG, "purchase")
  }

  companion object {
    val TAG = BillingClient::class.java.simpleName
  }
}
