package com.tatsuki.google.billing

import java.lang.IllegalStateException

enum class ConnectionState {
  DISCONNECTED,
  CONNECTING,
  CONNECTED,
  CLOSED,
  ;

  companion object {
    fun from(connectionState: Int): ConnectionState {
      return when (connectionState) {
        0 -> DISCONNECTED
        1 -> CONNECTING
        2 -> CONNECTED
        3 -> CLOSED
        else -> throw IllegalStateException("Unknown connection state: $connectionState")
      }
    }
  }
}
