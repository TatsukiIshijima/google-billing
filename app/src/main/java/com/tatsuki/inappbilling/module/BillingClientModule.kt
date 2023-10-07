package com.tatsuki.inappbilling.module

import android.content.Context
import com.android.billingclient.api.PurchasesUpdatedListener
import com.tatsuki.billing.core.GoogleBillingClientFactory
import com.tatsuki.billing.GoogleBillingClientFactoryImpl
import com.tatsuki.billing.GoogleBillingService
import com.tatsuki.billing.GoogleBillingServiceImpl
import com.tatsuki.billing.listener.PurchasesListener
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
