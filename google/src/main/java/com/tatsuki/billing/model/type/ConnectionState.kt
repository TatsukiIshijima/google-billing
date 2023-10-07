package com.tatsuki.billing.model.type

import com.android.billingclient.api.BillingClient
import java.lang.IllegalStateException

enum class ConnectionState {
  DISCONNECTED,
  CONNECTING,
  CONNECTED,
  CLOSED,
  ;

  companion object {
    fun from(connectionState: Int): ConnectionState {
      return when (connectionState) {
        BillingClient.ConnectionState.DISCONNECTED -> DISCONNECTED
        BillingClient.ConnectionState.CONNECTING -> CONNECTING
        BillingClient.ConnectionState.CONNECTED -> CONNECTED
        BillingClient.ConnectionState.CLOSED -> CLOSED
        else -> throw IllegalStateException("Unknown connection state: $connectionState")
      }
    }
  }
}
