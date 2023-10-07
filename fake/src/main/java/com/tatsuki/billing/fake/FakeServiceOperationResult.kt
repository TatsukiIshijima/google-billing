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
          FakeServiceStatus.Available -> {
            QueryProductDetailsResult(
              responseCode = BillingResponseCode.OK,
              productDetailsList = listOf(
                FakeProductDetails.create(
                  productId = Consts.IN_APP_PRODUCT_ID,
                  type = BillingClient.ProductType.INAPP,
                  subscriptionOfferDetails = null,
                ),
                FakeProductDetails.create(
                  productId = Consts.SUBSCRIPTION_PRODUCT_ID,
                  type = BillingClient.ProductType.SUBS,
                  oneTimePurchaseOfferDetails = null
                )
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
          is FakeServiceStatus.Available -> {
            UpdatePurchasesResult(
              responseCode = BillingResponseCode.OK,
              purchases = listOf(
                FakePurchase.create()
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
   * Result of consume process.
   */
  data class ConsumeResult(
    @BillingResponseCode override val responseCode: Int,
    val purchaseToken: String,
  ) : FakeServiceOperationResult {

    companion object {
      fun create(fakeServiceStatus: FakeServiceStatus): ConsumeResult {
        return when (fakeServiceStatus) {
          is FakeServiceStatus.Available -> {
            ConsumeResult(
              responseCode = BillingResponseCode.OK,
              purchaseToken = "purchaseToken",
            )
          }

          is FakeServiceStatus.UnAvailable -> {
            ConsumeResult(
              responseCode = BillingResponseCode.SERVICE_UNAVAILABLE,
              purchaseToken = ""
            )
          }
        }
      }
    }
  }
}