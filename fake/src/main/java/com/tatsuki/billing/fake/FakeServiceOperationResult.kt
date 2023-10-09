package com.tatsuki.billing.fake

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.tatsuki.billing.fake.model.FakeProductDetails
import com.tatsuki.billing.fake.model.FakePurchase

sealed interface FakeServiceOperationResult {

  @BillingResponseCode
  val responseCode: Int

  /**
   * Result of connect to google play.
   */
  data class ConnectionResult(
    @BillingResponseCode override val responseCode: Int
  ) : FakeServiceOperationResult {

    companion object {
      fun create(fakeServiceStatus: FakeServiceStatus): ConnectionResult {
        val responseCode = when (fakeServiceStatus) {
          is FakeServiceStatus.Available -> BillingResponseCode.OK
          is FakeServiceStatus.UnAvailable -> BillingResponseCode.SERVICE_UNAVAILABLE
        }
        return ConnectionResult(responseCode)
      }
    }
  }

  /**
   * Result of products available to buy.
   */
  data class QueryProductDetailsResult(
    @BillingResponseCode override val responseCode: Int,
    val productDetailsList: List<FakeProductDetails>?
  ) : FakeServiceOperationResult {

    companion object {
      fun create(fakeServiceStatus: FakeServiceStatus): QueryProductDetailsResult {
        return when (fakeServiceStatus) {
          is FakeServiceStatus.Available.Subscription -> {
            QueryProductDetailsResult(
              responseCode = BillingResponseCode.OK,
              productDetailsList = listOf(
                FakeProductDetails.create(
                  productId = Consts.SUBSCRIPTION_PRODUCT_ID,
                  type = BillingClient.ProductType.SUBS,
                  oneTimePurchaseOfferDetails = null
                )
              )
            )
          }

          is FakeServiceStatus.Available.InApp -> {
            QueryProductDetailsResult(
              responseCode = BillingResponseCode.OK,
              productDetailsList = listOf(
                FakeProductDetails.create(
                  productId = Consts.IN_APP_PRODUCT_ID,
                  type = BillingClient.ProductType.INAPP,
                  subscriptionOfferDetails = null,
                ),
              )
            )
          }

          FakeServiceStatus.UnAvailable ->
            QueryProductDetailsResult(
              responseCode = BillingResponseCode.SERVICE_UNAVAILABLE,
              productDetailsList = null
            )
        }
      }
    }
  }

  /**
   * Result of query purchase history.
   */
  data class QueryPurchaseHistoryResult(
    @BillingResponseCode override val responseCode: Int
  ) : FakeServiceOperationResult {

    companion object {
      fun create(fakeServiceStatus: FakeServiceStatus): QueryPurchaseHistoryResult {
        return when (fakeServiceStatus) {
          is FakeServiceStatus.Available -> {
            QueryPurchaseHistoryResult(
              responseCode = BillingResponseCode.OK,
            )
          }

          is FakeServiceStatus.UnAvailable -> {
            QueryPurchaseHistoryResult(
              responseCode = BillingResponseCode.SERVICE_UNAVAILABLE,
            )
          }
        }
      }
    }
  }

  /**
   * Result of query purchases.
   */
  data class QueryPurchasesResult(
    @BillingResponseCode override val responseCode: Int,
  ) : FakeServiceOperationResult {

    companion object {
      fun create(fakeServiceStatus: FakeServiceStatus): QueryPurchasesResult {
        return when (fakeServiceStatus) {
          is FakeServiceStatus.Available -> {
            QueryPurchasesResult(
              responseCode = BillingResponseCode.OK,
            )
          }

          is FakeServiceStatus.UnAvailable -> {
            QueryPurchasesResult(
              responseCode = BillingResponseCode.SERVICE_UNAVAILABLE,
            )
          }
        }
      }
    }
  }

  /**
   * Result of launch billing flow.
   */
  data class LaunchBillingFlowResult(
    @BillingResponseCode override val responseCode: Int,
    val updatePurchasesResult: UpdatePurchasesResult,
  ) : FakeServiceOperationResult {

    companion object {
      fun create(fakeServiceStatus: FakeServiceStatus): LaunchBillingFlowResult {
        val responseCode = when (fakeServiceStatus) {
          is FakeServiceStatus.Available -> BillingResponseCode.OK
          is FakeServiceStatus.UnAvailable -> BillingResponseCode.SERVICE_UNAVAILABLE
        }
        return LaunchBillingFlowResult(
          responseCode = responseCode,
          updatePurchasesResult = UpdatePurchasesResult.create(fakeServiceStatus)
        )
      }
    }
  }

  /**
   * Result of purchase process.
   */
  data class UpdatePurchasesResult(
    @BillingResponseCode override val responseCode: Int,
    val purchases: List<FakePurchase>
  ) : FakeServiceOperationResult {

    companion object {
      fun create(fakeServiceStatus: FakeServiceStatus): UpdatePurchasesResult {
        return when (fakeServiceStatus) {
          is FakeServiceStatus.Available.Subscription -> {
            UpdatePurchasesResult(
              responseCode = BillingResponseCode.OK,
              purchases = listOf(
                FakePurchase.create(
                  products = listOf(Consts.SUBSCRIPTION_PRODUCT_ID),
                )
              )
            )
          }

          is FakeServiceStatus.Available.InApp -> {
            UpdatePurchasesResult(
              responseCode = BillingResponseCode.OK,
              purchases = listOf(
                FakePurchase.create(
                  products = listOf(Consts.IN_APP_PRODUCT_ID),
                )
              )
            )
          }

          is FakeServiceStatus.UnAvailable -> {
            UpdatePurchasesResult(
              responseCode = BillingResponseCode.SERVICE_UNAVAILABLE,
              purchases = emptyList()
            )
          }
        }
      }
    }
  }

  /**
   * Result of acknowledge process.
   */
  data class AcknowledgeResult(
    override val responseCode: Int,
  ) : FakeServiceOperationResult {

    companion object {
      fun create(fakeServiceStatus: FakeServiceStatus): AcknowledgeResult {
        return when (fakeServiceStatus) {
          is FakeServiceStatus.Available.Subscription -> {
            AcknowledgeResult(
              responseCode = BillingResponseCode.OK,
            )
          }

          is FakeServiceStatus.Available.InApp -> {
            AcknowledgeResult(
              responseCode = BillingResponseCode.DEVELOPER_ERROR,
            )
          }

          is FakeServiceStatus.UnAvailable -> {
            AcknowledgeResult(
              responseCode = BillingResponseCode.SERVICE_UNAVAILABLE,
            )
          }
        }
      }
    }
  }

  /**
   * Result of consume process.
   */
  data class ConsumeResult(
    @BillingResponseCode override val responseCode: Int,
  ) : FakeServiceOperationResult {

    companion object {
      fun create(fakeServiceStatus: FakeServiceStatus): ConsumeResult {
        return when (fakeServiceStatus) {
          is FakeServiceStatus.Available.Subscription -> {
            ConsumeResult(
              responseCode = BillingResponseCode.DEVELOPER_ERROR,
            )
          }

          is FakeServiceStatus.Available.InApp -> {
            ConsumeResult(
              responseCode = BillingResponseCode.OK,
            )
          }

          is FakeServiceStatus.UnAvailable -> {
            ConsumeResult(
              responseCode = BillingResponseCode.SERVICE_UNAVAILABLE,
            )
          }
        }
      }
    }
  }

  /**
   * Result of feature support.
   */
  data class FeatureSupportResult(
    @BillingResponseCode override val responseCode: Int
  ) : FakeServiceOperationResult {

    companion object {
      fun create(fakeServiceStatus: FakeServiceStatus): FeatureSupportResult {
        return when (fakeServiceStatus) {
          is FakeServiceStatus.Available -> {
            FeatureSupportResult(
              responseCode = BillingResponseCode.OK,
            )
          }

          is FakeServiceStatus.UnAvailable -> {
            FeatureSupportResult(
              responseCode = BillingResponseCode.SERVICE_UNAVAILABLE,
            )
          }
        }
      }
    }
  }
}