package com.tatsuki.billing.fake

data class FakeServiceOperationResults(
  val connectionResult: FakeServiceOperationResult.ConnectionResult,
  val queryProductDetailsResult: FakeServiceOperationResult.QueryProductDetailsResult,
  val launchBillingFlowResult: FakeServiceOperationResult.LaunchBillingFlowResult,
  val updatePurchasesResult: FakeServiceOperationResult.UpdatePurchasesResult,
  val consumeResult: FakeServiceOperationResult.ConsumeResult? = null,
) {

  companion object {
    fun create(serviceStatus: FakeServiceStatus): FakeServiceOperationResults {
      return FakeServiceOperationResults(
        connectionResult = FakeServiceOperationResult.ConnectionResult.create(serviceStatus),
        queryProductDetailsResult = FakeServiceOperationResult.QueryProductDetailsResult.create(
          serviceStatus
        ),
        launchBillingFlowResult = FakeServiceOperationResult.LaunchBillingFlowResult.create(
          serviceStatus
        ),
        updatePurchasesResult = FakeServiceOperationResult.UpdatePurchasesResult.create(serviceStatus),
        consumeResult = FakeServiceOperationResult.ConsumeResult.create(serviceStatus),
      )
    }
  }
}
