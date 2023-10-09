package com.tatsuki.billing.fake

import android.app.Activity
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ConsumeResult
import com.android.billingclient.api.ProductDetailsResult
import com.android.billingclient.api.PurchaseHistoryResult
import com.android.billingclient.api.PurchasesResult
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchaseHistoryParams
import com.android.billingclient.api.QueryPurchasesParams
import com.tatsuki.billing.core.GoogleBillingClient
import com.tatsuki.billing.fake.model.FakePurchase
import com.tatsuki.billing.fake.model.toFakePurchaseHistoryRecord

class FakeGoogleBillingClient(
  private val purchasesUpdatedListener: PurchasesUpdatedListener,
) : GoogleBillingClient {

  private var status: FakeServiceStatus = FakeServiceStatus.Available.Subscription

  fun setup(fakeServiceStatus: FakeServiceStatus) {
    status = fakeServiceStatus
  }

  // Whether connected to google play store.
  private var _isReady: Boolean = false

  override val isReady: Boolean
    get() = _isReady

  // Cache purchases in google play store app.
  private var purchases: MutableList<FakePurchase> = mutableListOf()

  // All purchases are such as expired, canceled, acknowledged or consumed.
  private var recordPurchases: MutableList<FakePurchase> = mutableListOf()

  override fun connect(listener: BillingClientStateListener) {
    when (status) {
      is FakeServiceStatus.Available -> {
        val responseCode = FakeServiceOperationResults.create(status).connectionResult.responseCode
        _isReady = responseCode == BillingResponseCode.OK
        listener.onBillingSetupFinished(
          BillingResult.newBuilder()
            .setResponseCode(responseCode)
            .build()
        )
      }

      is FakeServiceStatus.UnAvailable -> {
        _isReady = false
        listener.onBillingServiceDisconnected()
      }
    }
  }

  override fun disconnect() {
    if (_isReady) {
      _isReady = false
    }
  }

  override suspend fun queryProductDetails(params: QueryProductDetailsParams): ProductDetailsResult {
    if (!_isReady) {
      return ProductDetailsResult(
        billingResult = BillingResult.newBuilder()
          .setResponseCode(BillingResponseCode.SERVICE_DISCONNECTED)
          .build(),
        productDetailsList = null
      )
    }
    val queryProductDetailsResult =
      FakeServiceOperationResults.create(status).queryProductDetailsResult
    return ProductDetailsResult(
      billingResult = BillingResult.newBuilder()
        .setResponseCode(queryProductDetailsResult.responseCode)
        .build(),
      productDetailsList = queryProductDetailsResult.productDetailsList?.map { it.toReal() }
    )
  }

  override suspend fun queryPurchaseHistory(params: QueryPurchaseHistoryParams): PurchaseHistoryResult {
    if (!_isReady) {
      return PurchaseHistoryResult(
        billingResult = BillingResult.newBuilder()
          .setResponseCode(BillingResponseCode.SERVICE_DISCONNECTED)
          .build(),
        purchaseHistoryRecordList = null
      )
    }
    val queryPurchaseHistoryResult =
      FakeServiceOperationResults.create(status).queryPurchaseHistoryResult
    val purchaseHistoryRecords =
      if (queryPurchaseHistoryResult.responseCode == BillingResponseCode.OK) {
        val allPurchases = (purchases + recordPurchases)
        when (status) {
          FakeServiceStatus.Available.InApp -> {
            allPurchases
              // Purchase type cannot be determined from QueryPurchaseHistoryParams.
              // So it is filtered by product id.
              .filter { it.products.contains(Consts.IN_APP_PRODUCT_ID) }
              .map { it.toFakePurchaseHistoryRecord() }
              .map { it.toReal() }
          }

          FakeServiceStatus.Available.Subscription -> {
            allPurchases
              // Purchase type cannot be determined from QueryPurchaseHistoryParams.
              // So it is filtered by product id.
              .filter { it.products.contains(Consts.SUBSCRIPTION_PRODUCT_ID) }
              .map { it.toFakePurchaseHistoryRecord() }
              .map { it.toReal() }
          }

          FakeServiceStatus.UnAvailable -> emptyList()
        }
      } else {
        emptyList()
      }
    return PurchaseHistoryResult(
      billingResult = BillingResult.newBuilder()
        .setResponseCode(queryPurchaseHistoryResult.responseCode)
        .build(),
      purchaseHistoryRecordList = purchaseHistoryRecords
    )
  }

  override suspend fun queryPurchases(params: QueryPurchasesParams): PurchasesResult {
    if (!_isReady) {
      return PurchasesResult(
        billingResult = BillingResult.newBuilder()
          .setResponseCode(BillingResponseCode.SERVICE_UNAVAILABLE)
          .build(),
        purchasesList = emptyList()
      )
    }
    val queryPurchasesResult = FakeServiceOperationResults.create(status).queryPurchasesResult
    val purchaseList = if (queryPurchasesResult.responseCode == BillingResponseCode.OK) {
      when (status) {
        FakeServiceStatus.Available.InApp -> {
          purchases
            // Purchase type cannot be determined from QueryPurchaseHistoryParams.
            // So it is filtered by product id.
            .filter { it.products.contains(Consts.IN_APP_PRODUCT_ID) }
            .map { it.toReal() }
        }

        FakeServiceStatus.Available.Subscription -> {
          purchases
            // Purchase type cannot be determined from QueryPurchaseHistoryParams.
            // So it is filtered by product id.
            .filter { it.products.contains(Consts.SUBSCRIPTION_PRODUCT_ID) }
            .map { it.toReal() }
        }

        FakeServiceStatus.UnAvailable -> emptyList()
      }
    } else {
      emptyList()
    }
    return PurchasesResult(
      billingResult = BillingResult.newBuilder()
        .setResponseCode(queryPurchasesResult.responseCode)
        .build(),
      purchasesList = purchaseList
    )
  }

  override fun launchBillingFlow(params: BillingFlowParams, activity: Activity): BillingResult {
    if (!_isReady) {
      return BillingResult.newBuilder()
        .setResponseCode(BillingResponseCode.SERVICE_DISCONNECTED)
        .build()
    }
    val launchBillingFlowResult = FakeServiceOperationResults.create(status).launchBillingFlowResult
    val updatePurchasesResult = FakeServiceOperationResults.create(status).updatePurchasesResult

    if (launchBillingFlowResult.responseCode == BillingResponseCode.OK) {
      // Return purchases only if successful launch billing flow.
      purchases.addAll(updatePurchasesResult.purchases)
      purchasesUpdatedListener.onPurchasesUpdated(
        BillingResult.newBuilder()
          .setResponseCode(updatePurchasesResult.responseCode)
          .build(),
        purchases.map { it.toReal() }
      )
    }
    return BillingResult.newBuilder()
      .setResponseCode(launchBillingFlowResult.responseCode)
      .build()
  }

  override suspend fun consumePurchase(params: ConsumeParams): ConsumeResult {
    if (!_isReady) {
      return ConsumeResult(
        billingResult = BillingResult.newBuilder()
          .setResponseCode(BillingResponseCode.SERVICE_UNAVAILABLE)
          .build(),
        purchaseToken = null
      )
    }
    val shouldConsumePurchase = purchases.find { it.purchaseToken == params.purchaseToken }
      ?: return ConsumeResult(
        billingResult = BillingResult.newBuilder()
          .setResponseCode(BillingResponseCode.DEVELOPER_ERROR)
          .build(),
        purchaseToken = null
      )
    val consumeResult = FakeServiceOperationResults.create(status).consumeResult
    if (consumeResult.responseCode == BillingResponseCode.OK) {
      // Remove purchase from cache in google play store app if successful consume.
      purchases.remove(shouldConsumePurchase)
      recordPurchases.add(shouldConsumePurchase)
    }
    return ConsumeResult(
      billingResult = BillingResult.newBuilder()
        .setResponseCode(consumeResult.responseCode)
        .build(),
      purchaseToken = shouldConsumePurchase.purchaseToken
    )
  }

  override suspend fun acknowledgePurchase(params: AcknowledgePurchaseParams): BillingResult {
    if (!_isReady) {
      return BillingResult.newBuilder()
        .setResponseCode(BillingResponseCode.SERVICE_UNAVAILABLE)
        .build()
    }
    val shouldAcknowledgePurchase = purchases.find { it.purchaseToken == params.purchaseToken }
      ?: return BillingResult.newBuilder()
        .setResponseCode(BillingResponseCode.DEVELOPER_ERROR)
        .build()
    val acknowledgeResult = FakeServiceOperationResults.create(status).acknowledgeResult
    if (acknowledgeResult.responseCode == BillingResponseCode.OK) {
      // Remove purchase from cache in google play store app if successful acknowledge.
      purchases.remove(shouldAcknowledgePurchase)
      val acknowledgedPurchase = shouldAcknowledgePurchase.copy(
        isAcknowledged = true
      )
      recordPurchases.add(acknowledgedPurchase)
    }
    return BillingResult.newBuilder()
      .setResponseCode(acknowledgeResult.responseCode)
      .build()
  }

  override fun isFeatureSupport(featureType: String): BillingResult {
    if (!_isReady) {
      return BillingResult.newBuilder()
        .setResponseCode(BillingResponseCode.SERVICE_UNAVAILABLE)
        .build()
    }
    val featureSupportResult = FakeServiceOperationResults.create(status).featureSupportResult
    return BillingResult.newBuilder()
      .setResponseCode(featureSupportResult.responseCode)
      .build()
  }
}