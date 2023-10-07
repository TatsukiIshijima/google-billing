package com.tatsuki.billing

import com.tatsuki.billing.fake.model.FakeOneTimePurchaseOfferDetails
import com.tatsuki.billing.fake.model.FakePricingPhase
import com.tatsuki.billing.fake.model.FakePricingPhases
import com.tatsuki.billing.fake.model.FakeProductDetails
import com.tatsuki.billing.fake.model.FakePurchase
import com.tatsuki.billing.fake.model.FakeSubscriptionOfferDetails
import org.junit.Test

class CreateFakeModelTest {

  @Test
  fun create_fake_pricing_phase() {
    val fakePricingPhase = FakePricingPhase.create()
    val realPricingPhase = fakePricingPhase.toReal()
    assert(fakePricingPhase.formattedPrice == realPricingPhase.formattedPrice)
  }

  @Test
  fun create_fake_pricing_phases() {
    val fakePricingPhases = FakePricingPhases.create()
    val realPricingPhases = fakePricingPhases.toReal()
    assert(fakePricingPhases.list.size == realPricingPhases.pricingPhaseList.size)
  }

  @Test
  fun create_fake_one_time_purchase_offer_details() {
    val fakeOneTimePurchaseOfferDetails = FakeOneTimePurchaseOfferDetails.create()
    val realOneTimePurchaseOfferDetails = fakeOneTimePurchaseOfferDetails.toReal()
    assert(fakeOneTimePurchaseOfferDetails.formattedPrice == realOneTimePurchaseOfferDetails.formattedPrice)
  }

  @Test
  fun create_fake_subscription_offer_details() {
    val fakeSubscriptionOfferDetails = FakeSubscriptionOfferDetails.create()
    val realSubscriptionOfferDetails = fakeSubscriptionOfferDetails.toReal()
    assert(fakeSubscriptionOfferDetails.basePlanId == realSubscriptionOfferDetails.basePlanId)
    assert(fakeSubscriptionOfferDetails.offerIdToken == realSubscriptionOfferDetails.offerToken)
    assert(fakeSubscriptionOfferDetails.offerTags.size == realSubscriptionOfferDetails.offerTags.size)
  }

  @Test
  fun create_fake_product_details() {
    val fakeProductDetails = FakeProductDetails.create()
    val realProductDetails = fakeProductDetails.toReal()
    assert(fakeProductDetails.productId == realProductDetails.productId)
    assert(fakeProductDetails.type == realProductDetails.productType)
    assert(fakeProductDetails.subscriptionOfferDetails?.size == realProductDetails.subscriptionOfferDetails?.size)
    assert(fakeProductDetails.oneTimePurchaseOfferDetails?.formattedPrice == realProductDetails.oneTimePurchaseOfferDetails?.formattedPrice)
  }

  @Test
  fun create_fake_purchase() {
    val fakePurchase = FakePurchase.create()
    val realPurchase = fakePurchase.toReal()
    assert(fakePurchase.products.first() == realPurchase.products.first())
    assert(fakePurchase.purchaseToken == realPurchase.purchaseToken)
    assert(fakePurchase.signature == realPurchase.signature)
    assert(fakePurchase.purchaseState == realPurchase.purchaseState)
    assert(fakePurchase.isAcknowledged == realPurchase.isAcknowledged)
  }
}