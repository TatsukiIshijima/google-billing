package com.tatsuki.google.billing.pattern

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.tatsuki.google.billing.ConnectionState

sealed interface ConnectionPattern {
  val isReady: Boolean
  val connectionState: ConnectionState

  sealed interface Connect : ConnectionPattern {
    val billingResult: BillingResult

    data class NotConnectedYet(
      override val isReady: Boolean = false,
      override val connectionState: ConnectionState = ConnectionState.CONNECTED,
      override val billingResult: BillingResult =
        BillingResult
          .newBuilder()
          .setResponseCode(BillingClient.BillingResponseCode.OK)
          .build(),
    ) : Connect

    data class AlreadyConnected(
      override val isReady: Boolean = true,
      override val connectionState: ConnectionState = ConnectionState.CONNECTED,
      override val billingResult: BillingResult =
        BillingResult
          .newBuilder()
          .setResponseCode(BillingClient.BillingResponseCode.OK)
          .build(),
    ) : Connect

    data class ServiceUnavailable(
      override val isReady: Boolean = false,
      override val connectionState: ConnectionState = ConnectionState.DISCONNECTED,
      override val billingResult: BillingResult =
        BillingResult
          .newBuilder()
          .setResponseCode(BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE)
          .build(),
    ) : Connect
  }

  sealed interface Disconnect : ConnectionPattern {
    data class NotDisConnectedYet(
      override val isReady: Boolean = true,
      override val connectionState: ConnectionState = ConnectionState.DISCONNECTED,
    ) : Disconnect

    data class AlreadyDisconnected(
      override val isReady: Boolean = false,
      override val connectionState: ConnectionState = ConnectionState.DISCONNECTED,
    ) : Disconnect
  }
}
