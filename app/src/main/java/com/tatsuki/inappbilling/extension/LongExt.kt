package com.tatsuki.inappbilling.extension

import android.text.format.DateFormat

fun Long.toDateString(): String {
  return DateFormat.format("yyyy/MM/dd HH:mm:ss", this).toString()
}