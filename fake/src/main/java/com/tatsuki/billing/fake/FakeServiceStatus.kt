package com.tatsuki.billing.fake

sealed interface FakeServiceStatus {

  object Available : FakeServiceStatus

  object UnAvailable : FakeServiceStatus
}