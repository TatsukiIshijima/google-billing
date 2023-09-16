package com.tatsuki.inappbilling.module

import android.content.Context
import com.android.billingclient.api.PurchasesUpdatedListener
import com.tatsuki.google.billing.GoogleBillingClientFactory
import com.tatsuki.google.billing.GoogleBillingClientFactoryImpl
import com.tatsuki.google.billing.GoogleBillingService
import com.tatsuki.google.billing.GoogleBillingServiceImpl
import com.tatsuki.google.billing.listener.PurchasesListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BillingClientModule {

  @Provides
  @Singleton
  fun providePurchasesUpdatedListener(): PurchasesUpdatedListener {
    return PurchasesListener
  }

  @Provides
  @Singleton
  fun provideGoogleBillingService(
    googleBillingClientFactory: GoogleBillingClientFactory
  ): GoogleBillingService {
    return GoogleBillingServiceImpl(googleBillingClientFactory)
  }

  @Provides
  @Singleton
  fun provideGoogleBillingClientFactory(
    @ApplicationContext context: Context,
    purchasesListener: PurchasesUpdatedListener
  ): GoogleBillingClientFactory {
    return GoogleBillingClientFactoryImpl(
      context,
      purchasesListener
    )
  }
}
