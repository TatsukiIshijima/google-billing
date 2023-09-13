package com.tatsuki.inappbilling

/**
 * https://github.com/android/play-billing-samples/blob/master/ClassyTaxiAppKotlin/app/src/main/java/com/example/billing/Constants.kt
 */
object Constants {

  // ProductIds
  const val TEST_IN_APP_BILLING_SUBSCRIPTION_PLAN_1 = "test.inappbilling.subscription.plan1"
  const val TEST_IN_APP_BILLING_SUBSCRIPTION_PLAN_2 = "test.inappbilling.subscription.plan2"
  const val TEST_IN_APP_BILLING_SUBSCRIPTION_PLAN_3 = "test.inappbilling.subscription.plan3"

  // PlanIds
  // Plan id same as plan id in this app.
  const val YEARLY_PLAN = "yearly-plan"
  const val MONTHLY_PLAN = "monthly-plan"
  const val WEEKLY_PLAN = "weekly-plan"
  const val BASIC_PLAN = "basic-plan"
  const val PREMIUM_PLAN = "premium-plan"
  const val ESSENTIAL_PLAN = "essential-plan"
  const val EXTRA_PLAN = "extra-plan"

  // OfferIds
  // Offer id same as offer id in this app.
  const val TRIAL = "trial"
  const val DISCOUNT = "discount"
}
