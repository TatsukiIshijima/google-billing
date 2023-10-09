package com.tatsuki.billing.fake

data class FakeServiceOperationResults(
  val connectionResult: FakeServiceOperationResult.ConnectionResult,
  val queryProductDetailsResult: FakeServiceOperationResult.QueryProductDetailsResult,
  val queryPurchaseHistoryResult: FakeServiceOperationResult.QueryPurchaseHistoryResult,
  val queryPurchasesResult: FakeServiceOperationResult.QueryPurchasesResult,
  val launchBillingFlowResult: FakeServiceOperationResult.LaunchBillingFlowResult,
  val updatePurchasesResult: FakeServiceOperationResult.UpdatePurchasesResult,
  val acknowledgeResult: FakeServiceOperationResult.AcknowledgeResult,
  val consumeResult: FakeServiceOperationResult.ConsumeResult,
  val featureSupportResult: FakeServiceOperationResult.FeatureSupportResult,
) {

  companion object {
    fun create(serviceStatus: FakeServiceStatus): FakeServiceOperationResults {
      return FakeServiceOperationResults(
        connectionResult = FakeServiceOperationResult.ConnectionResult.create(serviceStatus),
        queryProductDetailsResult = FakeServiceOperationResult.QueryProductDetailsResult.create(
          serviceStatus
        ),
        queryPurchaseHistoryResult = FakeServiceOperationResult.QueryPurchaseHistoryResult.create(
          serviceStatus
        ),
        queryPurchasesResult = FakeServiceOperationResult.QueryPurchasesResult.create(
          serviceStatus
        ),
        launchBillingFlowResult = FakeServiceOperationResult.LaunchBillingFlowResult.create(
          serviceStatus
        ),
        updatePurchasesResult = FakeServiceOperationResult.UpdatePurchasesResult.create(
          serviceStatus
        ),
        acknowledgeResult = FakeServiceOperationResult.AcknowledgeResult.create(serviceStatus),
        consumeResult = FakeServiceOperationResult.ConsumeResult.create(serviceStatus),
        featureSupportResult = FakeServiceOperationResult.FeatureSupportResult.create(serviceStatus),
      )
    }
  }
}
