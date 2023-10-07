package com.tatsuki.inappbilling

import com.tatsuki.billing.model.Product
import com.tatsuki.billing.model.ProductId
import com.tatsuki.billing.model.type.ProductType

/**
 * https://github.com/android/play-billing-samples/blob/master/ClassyTaxiAppKotlin/app/src/main/java/com/example/billing/Constants.kt
 */
object Constants {

  // ProductIds
  const val TEST_IN_APP_BILLING_SUBSCRIPTION_PLAN_1 = "test.inappbilling.subscription.plan1"
  const val TEST_IN_APP_BILLING_SUBSCRIPTION_PLAN_2 = "test.inappbilling.subscription.plan2"
  const val TEST_IN_APP_BILLING_SUBSCRIPTION_PLAN_3 = "test.inappbilling.subscription.plan3"
  const val TEST_IN_APP_BILLING_SUBSCRIPTION_PLAN_4 = "test.inappbilling.subscription.plan4"
  const val TEST_IN_APP_BILLING_SUBSCRIPTION_PLAN_5 = "test.inappbilling.subscription.plan5"

  // PlanIds
  // Plan id same as plan id in this app.
  const val YEARLY_PLAN = "yearly-plan"
  const val MONTHLY_PLAN = "monthly-plan"
  const val WEEKLY_PLAN = "weekly-plan"
  const val BASIC_PLAN = "basic-plan"
  const val PREMIUM_PLAN = "premium-plan"
  const val ESSENTIAL_PLAN = "essential-plan"
  const val EXTRA_PLAN = "extra-plan"
  const val SIMPLE_PLAN = "simple-plan"
  const val SIMPLE_PLUS_PLAN = "simple-plus-plan"
  const val STANDARD_PLAN = "standard-plan"

  // OfferIds
  // Offer id same as offer id in this app.
  const val TRIAL = "trial"
  const val DISCOUNT = "discount"

  // ConsumeItemIds
  const val TEST_IN_APP_BILLING_CONSUME_ITEM_1 = "test.inappbilling.consume.item1"
  const val TEST_IN_APP_BILLING_CONSUME_ITEM_2 = "test.inappbilling.consume.item2"
  const val TEST_IN_APP_BILLING_CONSUME_ITEM_3 = "test.inappbilling.consume.item3"

  val SubscriptionList = listOf(
    Product(
      ProductId(TEST_IN_APP_BILLING_SUBSCRIPTION_PLAN_1),
      ProductType.Subscription()
    ),
    Product(
      ProductId(TEST_IN_APP_BILLING_SUBSCRIPTION_PLAN_2),
      ProductType.Subscription()
    ),
    Product(
      ProductId(TEST_IN_APP_BILLING_SUBSCRIPTION_PLAN_3),
      ProductType.Subscription()
    ),
    Product(
      ProductId(TEST_IN_APP_BILLING_SUBSCRIPTION_PLAN_4),
      ProductType.Subscription()
    ),
    Product(
      ProductId(TEST_IN_APP_BILLING_SUBSCRIPTION_PLAN_5),
      ProductType.Subscription()
    ),
  )

  val InAppItemList = listOf(
    Product(
      ProductId(TEST_IN_APP_BILLING_CONSUME_ITEM_1),
      ProductType.InApp()
    ),
    Product(
      ProductId(TEST_IN_APP_BILLING_CONSUME_ITEM_2),
      ProductType.InApp()
    ),
    Product(
      ProductId(TEST_IN_APP_BILLING_CONSUME_ITEM_3),
      ProductType.InApp()
    )
  )
}
