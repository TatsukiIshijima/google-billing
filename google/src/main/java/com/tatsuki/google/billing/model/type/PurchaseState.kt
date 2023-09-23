package com.tatsuki.google.billing.model.type

import com.android.billingclient.api.Purchase

sealed interface PurchaseState {
  @Purchase.PurchaseState
  val value: Int

  data class UnspecifiedState(
    override val value: Int = Purchase.PurchaseState.UNSPECIFIED_STATE
  ) : PurchaseState

  data class Purchased(
    override val value: Int = Purchase.PurchaseState.PURCHASED
  ) : PurchaseState

  data class Pending(
    override val value: Int = Purchase.PurchaseState.PENDING
  ) : PurchaseState

  companion object {
    fun from(@Purchase.PurchaseState value: Int): PurchaseState {
      return when (value) {
        Purchase.PurchaseState.UNSPECIFIED_STATE -> UnspecifiedState()
        Purchase.PurchaseState.PURCHASED -> Purchased()
        Purchase.PurchaseState.PENDING -> Pending()
        else -> throw IllegalArgumentException("Unknown value: $value")
      }
    }
  }
}

fun PurchaseState.Companion.fake(
  state: PurchaseState = PurchaseState.Purchased()
): PurchaseState {
  return state
}