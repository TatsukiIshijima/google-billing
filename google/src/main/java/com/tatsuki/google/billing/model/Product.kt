package com.tatsuki.google.billing.model

import com.android.billingclient.api.QueryProductDetailsParams
import com.tatsuki.google.billing.model.type.ProductType

data class Product(
  val id: ProductId,
  val productType: ProductType
) {

  fun toQueryProduct(): QueryProductDetailsParams.Product {
    return QueryProductDetailsParams.Product.newBuilder()
      .setProductId(id.value)
      .setProductType(productType.value)
      .build()
  }
}
