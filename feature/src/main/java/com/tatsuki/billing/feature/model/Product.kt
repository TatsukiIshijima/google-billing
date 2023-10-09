package com.tatsuki.billing.feature.model

import com.android.billingclient.api.QueryProductDetailsParams
import com.tatsuki.billing.feature.model.type.ProductType

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
