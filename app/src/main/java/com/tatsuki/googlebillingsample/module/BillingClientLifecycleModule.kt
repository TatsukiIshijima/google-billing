package com.tatsuki.googlebillingsample.module

import android.app.Application
import com.tatsuki.googlebillingsample.BillingClientLifecycle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BillingClientLifecycleModule {

  @Provides
  @Singleton
  fun provideBillingLifecycle(
    application: Application
  ): BillingClientLifecycle {
    return BillingClientLifecycle.getInstance(application)
  }
}
