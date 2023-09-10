package com.tatsuki.google.billing

import com.tatsuki.google.billing.model.Product
import com.tatsuki.google.billing.model.ProductId
import com.tatsuki.google.billing.model.ProductType
import com.tatsuki.google.billing.pattern.AcknowledgePattern
import com.tatsuki.google.billing.pattern.ConnectionPattern
import com.tatsuki.google.billing.pattern.ConsumePattern
import com.tatsuki.google.billing.pattern.QueryProductDetailsPattern
import com.tatsuki.google.billing.pattern.QueryPurchaseHistoryPattern
import com.tatsuki.google.billing.pattern.QueryPurchasesPattern
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.lang.Exception

@OptIn(ExperimentalCoroutinesApi::class)
class GoogleBillingServiceTest {

  private lateinit var fakeGoogleBillingClientImpl: FakeGoogleBillingClientImpl
  private lateinit var googleBillingService: GoogleBillingServiceImpl

  @Before
  fun setup() {
    fakeGoogleBillingClientImpl = FakeGoogleBillingClientImpl()
    fakeGoogleBillingClientImpl.connectionCallCounter.reset()
    googleBillingService = GoogleBillingServiceImpl(fakeGoogleBillingClientImpl)
  }

  @Test
  fun callConnect_returnConnectedWhenNotConnectedYet() = runTest {
    fakeGoogleBillingClientImpl.setup(ConnectionPattern.Connect.NotConnectedYet())

    val connectionTask = async { googleBillingService.connect() }
    advanceUntilIdle()

    assert(ConnectionStateListener.connectionRequestId != null)
    assert(ConnectionStateListener.onBillingServiceConnectionListenerMap.size == 1)

    fakeGoogleBillingClientImpl.onBillingSetupFinished()
    val connectionState = connectionTask.await()

    assert(connectionState == ConnectionState.CONNECTED)
    assert(fakeGoogleBillingClientImpl.connectionCallCounter.connectCallCount == 1)
    assert(ConnectionStateListener.onBillingServiceConnectionListenerMap.isEmpty())
    assert(ConnectionStateListener.connectionRequestId == null)
  }

  @Test
  fun callConnect_returnConnectedWhenAlreadyConnected() = runTest {
    fakeGoogleBillingClientImpl.setup(ConnectionPattern.Connect.AlreadyConnected())

    val connectionState = googleBillingService.connect()

    advanceUntilIdle()

    assert(connectionState == ConnectionState.CONNECTED)
    assert(fakeGoogleBillingClientImpl.connectionCallCounter.connectCallCount == 0)
  }

  @Test
  fun callConnect_returnExceptionWhenConnectFailed() = runTest {
    fakeGoogleBillingClientImpl.setup(ConnectionPattern.Connect.ConnectFailed())

    val connectionTask = async {
      runCatching {
        googleBillingService.connect()
      }
    }
    advanceUntilIdle()

    assert(ConnectionStateListener.connectionRequestId != null)
    assert(ConnectionStateListener.onBillingServiceConnectionListenerMap.size == 1)

    fakeGoogleBillingClientImpl.onBillingSetupFinished()
    val actual = connectionTask.await().exceptionOrNull()

    assert(actual is GoogleBillingServiceException.ConnectFailedException)
    assert(fakeGoogleBillingClientImpl.connectionCallCounter.connectCallCount == 1)
    assert(ConnectionStateListener.onBillingServiceConnectionListenerMap.isEmpty())
    assert(ConnectionStateListener.connectionRequestId == null)

  }

  @Test
  fun callDisconnect_returnDisconnectedWhenNotDisconnectedYet() {
    fakeGoogleBillingClientImpl.setup(ConnectionPattern.Disconnect.NotDisConnectedYet())

    googleBillingService.disconnect()

    assert(fakeGoogleBillingClientImpl.connectionCallCounter.disconnectCallCount == 1)
  }

  @Test
  fun callDisconnect_returnDisconnectedWhenAlreadyDisconnected() {
    fakeGoogleBillingClientImpl.setup(ConnectionPattern.Disconnect.AlreadyDisconnected())

    googleBillingService.disconnect()

    assert(fakeGoogleBillingClientImpl.connectionCallCounter.disconnectCallCount == 0)
  }

  @Test
  fun callQueryProductDetails_returnProductDetailsListWhenSuccess() = runTest {
    fakeGoogleBillingClientImpl.setup(QueryProductDetailsPattern.Success())

    val task = async {
      googleBillingService.queryProductDetails(
        products = listOf(
          Product(
            id = ProductId("productId"),
            productType = ProductType.InApp()
          )
        )
      )
    }

    val result = task.await()
    assert(result !is Exception)
  }

  @Test
  fun callQueryProductDetails_returnExceptionWhenError() = runTest {
    fakeGoogleBillingClientImpl.setup(QueryProductDetailsPattern.Error())

    val task = async {
      runCatching {
        googleBillingService.queryProductDetails(
          products = listOf(
            Product(
              id = ProductId("productId"),
              productType = ProductType.InApp()
            )
          )
        )
      }
    }

    val result = task.await().exceptionOrNull()
    assert(result is GoogleBillingServiceException.ErrorException)
  }

  @Test
  fun callQueryPurchaseHistory_returnHistoryRecordListWhenSuccess() = runTest {
    fakeGoogleBillingClientImpl.setup(QueryPurchaseHistoryPattern.Success())

    val task = async {
      googleBillingService.queryPurchaseHistory(ProductType.Subscription())
    }

    val result = task.await()
    assert(result.isNotEmpty())
  }

  @Test
  fun callQueryPurchaseHistory_returnHistoryRecordListWhenFailure() = runTest {
    fakeGoogleBillingClientImpl.setup(QueryPurchaseHistoryPattern.Error())

    val task = async {
      runCatching {
        googleBillingService.queryPurchaseHistory(ProductType.Subscription())
      }
    }

    val result = task.await().exceptionOrNull()
    assert(result is GoogleBillingServiceException.ErrorException)
  }

  @Test
  fun callQueryPurchases_returnPurchaseListWhenSuccess() = runTest {
    fakeGoogleBillingClientImpl.setup(QueryPurchasesPattern.Success())

    val task = async {
      googleBillingService.queryPurchases(ProductType.Subscription())
    }

    val result = task.await()
    assert(result.isNotEmpty())
  }

  @Test
  fun callQueryPurchases_returnExceptionWhenServiceUnavailable() = runTest {
    fakeGoogleBillingClientImpl.setup(QueryPurchasesPattern.ServiceUnavailableError())

    val task = async {
      runCatching {
        googleBillingService.queryPurchases(ProductType.Subscription())
      }
    }

    val result = task.await().exceptionOrNull()
    assert(result is GoogleBillingServiceException.ServiceUnavailableException)
  }

  @Test
  fun callConsume_noExceptionWhenSuccess() = runTest {
    fakeGoogleBillingClientImpl.setup(ConsumePattern.Success())

    val task = async {
      runCatching {
        googleBillingService.consumePurchase("purchaseToken")
      }
    }

    val result = task.await().exceptionOrNull()
    assert(result == null)
  }

  @Test
  fun callConsume_exceptionWhenError() = runTest {
    fakeGoogleBillingClientImpl.setup(ConsumePattern.Error())

    val task = async {
      runCatching {
        googleBillingService.consumePurchase("")
      }
    }

    val result = task.await().exceptionOrNull()
    assert(result is GoogleBillingServiceException.ErrorException)
  }

  @Test
  fun callAcknowledge_noExceptionWhenSuccess() = runTest {
    fakeGoogleBillingClientImpl.setup(AcknowledgePattern.Success())

    val task = async {
      runCatching {
        googleBillingService.acknowledgePurchase("purchaseToken")
      }
    }

    val result = task.await().exceptionOrNull()
    assert(result == null)
  }

  @Test
  fun callAcknowledge_exceptionWhenError() = runTest {
    fakeGoogleBillingClientImpl.setup(AcknowledgePattern.Error())

    val task = async {
      runCatching {
        googleBillingService.acknowledgePurchase("")
      }
    }

    val result = task.await().exceptionOrNull()
    assert(result is GoogleBillingServiceException.ErrorException)
  }
}
