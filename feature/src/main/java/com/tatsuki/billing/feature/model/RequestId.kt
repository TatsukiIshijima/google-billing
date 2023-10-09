package com.tatsuki.billing.feature.model

import java.util.UUID

data class RequestId(
  val value: UUID = UUID.randomUUID()
)
