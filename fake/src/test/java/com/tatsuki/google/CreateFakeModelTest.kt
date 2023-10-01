package com.tatsuki.google

import com.tatsuki.google.billing.fake.model.FakeOneTimePurchaseOfferDetails
import com.tatsuki.google.billing.fake.model.FakePricingPhase
import com.tatsuki.google.billing.fake.model.FakePricingPhases
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
}