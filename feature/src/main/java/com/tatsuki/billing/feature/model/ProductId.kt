package com.tatsuki.billing.feature.model

data class ProductId(
  val value: String
) {
  companion object
}

fun ProductId.Companion.fake(
  value: String = "productId",
): ProductId {
  return ProductId(
    value = value,
  )
}
