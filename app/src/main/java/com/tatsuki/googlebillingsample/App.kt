package com.tatsuki.googlebillingsample

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {

  val billingClientLifecycle: BillingClientLifecycle
    get() = BillingClientLifecycle.getInstance(this)
}
