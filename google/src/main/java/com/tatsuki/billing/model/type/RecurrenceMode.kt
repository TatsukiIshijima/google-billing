package com.tatsuki.billing.model.type

import com.android.billingclient.api.ProductDetails

sealed interface RecurrenceMode {
  @ProductDetails.RecurrenceMode
  val value: Int

  data class InfiniteRecurring(
    override val value: Int = ProductDetails.RecurrenceMode.INFINITE_RECURRING
  ) : RecurrenceMode

  data class FiniteRecurring(
    override val value: Int = ProductDetails.RecurrenceMode.FINITE_RECURRING
  ) : RecurrenceMode

  data class NonRecurring(
    override val value: Int = ProductDetails.RecurrenceMode.NON_RECURRING
  ) : RecurrenceMode

  companion object {
    fun from(@ProductDetails.RecurrenceMode value: Int): RecurrenceMode {
      return when (value) {
        ProductDetails.RecurrenceMode.INFINITE_RECURRING -> InfiniteRecurring()
        ProductDetails.RecurrenceMode.FINITE_RECURRING -> FiniteRecurring()
        ProductDetails.RecurrenceMode.NON_RECURRING -> NonRecurring()
        else -> throw IllegalArgumentException("Unknown value: $value")
      }
    }
  }
}