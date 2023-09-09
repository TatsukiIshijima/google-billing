package com.tatsuki.google.billing

sealed class GoogleBillingServiceException(
  override val message: String,
  open val responseCode: Int,
): Exception(message) {

  class ConnectFailedException(
    override val message: String = "Failed to connect Google Play.",
    override val responseCode: Int,
  ): GoogleBillingServiceException(message, responseCode)
}
