package com.tatsuki.billing.fake

sealed interface FakeServiceStatus {

  sealed interface Available : FakeServiceStatus {
    object Subscription : Available

    object InApp : Available
  }

  object UnAvailable : FakeServiceStatus
}