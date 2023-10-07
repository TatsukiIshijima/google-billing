package com.tatsuki.billing

import com.android.billingclient.api.BillingClient.BillingResponseCode

sealed class GoogleBillingServiceException(
  override val message: String,
  open val responseCode: Int,
) : Exception(message) {

  class FeatureNotSupportedException(
    override val message: String = "The requested feature is not supported by the Play Store on the current device.",
    override val responseCode: Int = BillingResponseCode.FEATURE_NOT_SUPPORTED
  ) : GoogleBillingServiceException(message, responseCode)

  class ServiceDisconnectedException(
    override val message: String = "The app is not connected to the Play Store service via the Google Play Billing Library.",
    override val responseCode: Int = BillingResponseCode.SERVICE_DISCONNECTED
  ) : GoogleBillingServiceException(message, responseCode)

  class UserCanceledException(
    override val message: String = "Transaction was canceled by the user.",
    override val responseCode: Int = BillingResponseCode.USER_CANCELED
  ) : GoogleBillingServiceException(message, responseCode)

  class ServiceUnavailableException(
    override val message: String = "The service is currently unavailable.",
    override val responseCode: Int = BillingResponseCode.SERVICE_UNAVAILABLE
  ) : GoogleBillingServiceException(message, responseCode)

  class BillingUnavailableException(
    override val message: String = "A user billing error occurred during processing.",
    override val responseCode: Int = BillingResponseCode.BILLING_UNAVAILABLE,
  ) : GoogleBillingServiceException(message, responseCode)

  class ItemUnavailableException(
    override val message: String = "The requested product is not available for purchase.",
    override val responseCode: Int = BillingResponseCode.ITEM_UNAVAILABLE
  ) : GoogleBillingServiceException(message, responseCode)

  class DeveloperErrorException(
    override val message: String = "Error resulting from incorrect usage of the API.",
    override val responseCode: Int = BillingResponseCode.DEVELOPER_ERROR
  ) : GoogleBillingServiceException(message, responseCode)

  class ErrorException(
    override val message: String = "Fatal error during the API action.",
    override val responseCode: Int = BillingResponseCode.ERROR
  ) : GoogleBillingServiceException(message, responseCode)

  class ItemAlreadyOwnedException(
    override val message: String = "The purchase failed because the item is already owned.",
    override val responseCode: Int = BillingResponseCode.ITEM_ALREADY_OWNED
  ) : GoogleBillingServiceException(message, responseCode)

  class ItemNotOwnedException(
    override val message: String = "Requested action on the item failed since it is not owned by the user.",
    override val responseCode: Int = BillingResponseCode.ITEM_NOT_OWNED
  ) : GoogleBillingServiceException(message, responseCode)

  class NetworkErrorException(
    override val message: String = "A network error occurred during the operation.",
    override val responseCode: Int = BillingResponseCode.NETWORK_ERROR
  ) : GoogleBillingServiceException(message, responseCode)

  class UnKnownException(
    override val message: String = "Unknown.",
    override val responseCode: Int = 1000
  ) : GoogleBillingServiceException(message, responseCode)
}

fun Int.toException(): GoogleBillingServiceException {
  return when (this) {
    BillingResponseCode.FEATURE_NOT_SUPPORTED -> {
      GoogleBillingServiceException.FeatureNotSupportedException()
    }

    BillingResponseCode.SERVICE_DISCONNECTED -> {
      GoogleBillingServiceException.ServiceDisconnectedException()
    }

    BillingResponseCode.USER_CANCELED -> {
      GoogleBillingServiceException.UserCanceledException()
    }

    BillingResponseCode.SERVICE_UNAVAILABLE -> {
      GoogleBillingServiceException.ServiceUnavailableException()
    }

    BillingResponseCode.BILLING_UNAVAILABLE -> {
      GoogleBillingServiceException.BillingUnavailableException()
    }

    BillingResponseCode.ITEM_UNAVAILABLE -> {
      GoogleBillingServiceException.ItemUnavailableException()
    }

    BillingResponseCode.DEVELOPER_ERROR -> {
      GoogleBillingServiceException.DeveloperErrorException()
    }

    BillingResponseCode.ERROR -> {
      GoogleBillingServiceException.ErrorException()
    }

    BillingResponseCode.ITEM_ALREADY_OWNED -> {
      GoogleBillingServiceException.ItemAlreadyOwnedException()
    }

    BillingResponseCode.ITEM_NOT_OWNED -> {
      GoogleBillingServiceException.ItemNotOwnedException()
    }

    BillingResponseCode.NETWORK_ERROR -> {
      GoogleBillingServiceException.NetworkErrorException()
    }

    else -> {
      GoogleBillingServiceException.UnKnownException()
    }
  }
}
