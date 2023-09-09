package com.tatsuki.google

import com.tatsuki.google.FakeGoogleBillingClientImpl.ConnectionPattern
import com.tatsuki.google.billing.ConnectionState
import com.tatsuki.google.billing.GoogleBillingServiceImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

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

    val result = async { googleBillingService.connect() }
    advanceUntilIdle()

    assert(googleBillingService.connectionListener.connectionRequestId != null)
    assert(googleBillingService.connectionListener.onBillingServiceConnectionListenerMap.size == 1)

    fakeGoogleBillingClientImpl.onBillingSetupFinished()
    val connectionState = result.await()

    assert(connectionState == ConnectionState.CONNECTED)
    assert(fakeGoogleBillingClientImpl.connectionCallCounter.connectCallCount == 1)
    assert(googleBillingService.connectionListener.onBillingServiceConnectionListenerMap.isEmpty())
    assert(googleBillingService.connectionListener.connectionRequestId == null)
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
}
