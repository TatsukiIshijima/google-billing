package com.tatsuki.billing.model

import java.util.UUID

data class RequestId(
  val value: UUID = UUID.randomUUID()
)
