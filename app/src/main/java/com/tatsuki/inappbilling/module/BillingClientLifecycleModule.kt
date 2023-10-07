package com.tatsuki.inappbilling.module

import com.tatsuki.billing.GoogleBillingService
import com.tatsuki.inappbilling.BillingClientLifecycle
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
    googleBillingService: GoogleBillingService,
  ): BillingClientLifecycle {
    return BillingClientLifecycle.getInstance(googleBillingService)
  }
}
