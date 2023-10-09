package com.tatsuki.billing.feature

import android.app.Activity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.billingclient.api.Purchase
import com.tatsuki.billing.TestActivity
import com.tatsuki.billing.fake.Consts
import com.tatsuki.billing.fake.FakeBillingClientFactory
import com.tatsuki.billing.fake.FakeGoogleBillingClient
import com.tatsuki.billing.fake.FakeServiceStatus
import com.tatsuki.billing.feature.listener.ConnectionStateListener
import com.tatsuki.billing.feature.listener.PurchasesListener
import com.tatsuki.billing.feature.model.Product
import com.tatsuki.billing.feature.model.ProductId
import com.tatsuki.billing.feature.model.type.ProductType
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import java.lang.ref.WeakReference

@RunWith(AndroidJUnit4::class)
class GoogleBillingServiceTest {

  private lateinit var fakeGoogleBillingClient: FakeGoogleBillingClient
  private lateinit var googleBillingService: GoogleBillingServiceImpl
  private lateinit var testActivity: Activity

  @Before
  fun setup() {
    val fakeBillingClientFactory = FakeBillingClientFactory()
    googleBillingService = GoogleBillingServiceImpl(
      googleBillingFactory = fakeBillingClientFactory,
      connectionStateListener = ConnectionStateListener,
      purchasesListener = PurchasesListener,
    )

    fakeGoogleBillingClient = fakeBillingClientFactory.fakeGoogleBillingClient
    testActivity = Robolectric.buildActivity(TestActivity::class.java).create().get()
  }

  /**
   * Test of subscription purchase flow.
   */
  @Test
  fun purchaseSubscription() = runTest {
    fakeGoogleBillingClient.setup(FakeServiceStatus.Available.Subscription)

    // Connect to google play store app.
    googleBillingService.connect()
    assert(fakeGoogleBillingClient.isReady)

    // Query product details defined in google play console based on product type.
    val productDetails = googleBillingService.queryProductDetails(
      products = listOf(
        Product(
          id = ProductId(Consts.SUBSCRIPTION_PRODUCT_ID),
          productType = ProductType.Subscription(),
        ),
      )
    )
    assert(productDetails.size == 1)

    // Select product details to purchase.
    val selectedProductDetails = productDetails.first()
    val offerToken = selectedProductDetails.subscriptionOfferDetails?.first()?.offerToken ?: ""

    // Execute purchase subscription.
    val purchaseSubscription = googleBillingService.purchaseSubscription(
      productDetails = selectedProductDetails,
      offerToken = offerToken,
      activityRef = WeakReference(testActivity)
    ).first()
    assert(purchaseSubscription.products.first() == Consts.SUBSCRIPTION_PRODUCT_ID)
    assert(purchaseSubscription.purchaseState == Purchase.PurchaseState.PURCHASED)
    assert(!purchaseSubscription.isAcknowledged)

    // Fetch purchase to acknowledge.
    val purchases = googleBillingService.queryPurchases(ProductType.Subscription())
    val purchaseHistoryNotYetAcknowledged = googleBillingService.queryPurchaseHistory(ProductType.Subscription())
    assert(purchases.size == 1)
    assert(purchaseHistoryNotYetAcknowledged.size == 1)

    // Acknowledge purchase.
    googleBillingService.acknowledgePurchase(purchaseSubscription.purchaseToken)
    val acknowledgedPurchases = googleBillingService.queryPurchases(ProductType.Subscription())
    val purchaseHistoryAcknowledged = googleBillingService.queryPurchaseHistory(ProductType.Subscription())
    assert(acknowledgedPurchases.isEmpty())
    assert(purchaseHistoryAcknowledged.size == 1)

    // Disconnect from google play store app.
    googleBillingService.disconnect()
    assert(!fakeGoogleBillingClient.isReady)
  }

  /**
   * Test of in-app (consumable item) purchase flow.
   */
  @Test
  fun purchaseInApp() = runTest {
    fakeGoogleBillingClient.setup(FakeServiceStatus.Available.InApp)

    // Connect to google play store app.
    googleBillingService.connect()
    assert(fakeGoogleBillingClient.isReady)

    // Query product details defined in google play console based on product type.
    val productDetails = googleBillingService.queryProductDetails(
      products = listOf(
        Product(
          id = ProductId(Consts.IN_APP_PRODUCT_ID),
          productType = ProductType.InApp(),
        ),
      )
    )
    assert(productDetails.size == 1)

    // Select product details to purchase.
    val selectedProductDetails = productDetails.first()

    // Execute purchase subscription.
    val purchaseInApp = googleBillingService.purchaseConsumableProduct(
      productDetails = selectedProductDetails,
      activityRef = WeakReference(testActivity)
    )
    assert(purchaseInApp?.first()?.products?.first() == Consts.IN_APP_PRODUCT_ID)
    assert(purchaseInApp?.first()?.purchaseState == Purchase.PurchaseState.PURCHASED)

    // Fetch purchase to consume.
    val purchases = googleBillingService.queryPurchases(ProductType.InApp())
    val purchaseHistoryNotYetConsumed = googleBillingService.queryPurchaseHistory(ProductType.InApp())
    assert(purchases.size == 1)
    assert(purchaseHistoryNotYetConsumed.size == 1)

    // Consume purchase.
    googleBillingService.consumePurchase(purchaseInApp?.first()?.purchaseToken ?: "")
    val consumedPurchases = googleBillingService.queryPurchases(ProductType.InApp())
    val purchaseHistoryConsumed = googleBillingService.queryPurchaseHistory(ProductType.InApp())
    assert(consumedPurchases.isEmpty())
    assert(purchaseHistoryConsumed.size == 1)

    // Disconnect from google play store app.
    googleBillingService.disconnect()
    assert(!fakeGoogleBillingClient.isReady)
  }
}
