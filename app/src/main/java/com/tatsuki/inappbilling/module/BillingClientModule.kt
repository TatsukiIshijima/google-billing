package com.tatsuki.inappbilling.module

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.PurchasesUpdatedListener
import com.tatsuki.google.billing.GoogleBillingClient
import com.tatsuki.google.billing.GoogleBillingClientImpl
import com.tatsuki.google.billing.GoogleBillingService
import com.tatsuki.google.billing.GoogleBillingServiceImpl
import com.tatsuki.google.billing.PurchasesListener
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
  fun provideBillingClient(
    @ApplicationContext context: Context,
    purchasesListener: PurchasesUpdatedListener,
  ): BillingClient {
    return BillingClient.newBuilder(context)
      .enablePendingPurchases()
      .setListener(purchasesListener)
      .build()
  }

  @Provides
  @Singleton
  fun providePurchasesUpdatedListener(): PurchasesUpdatedListener {
    return PurchasesListener
  }

  @Provides
  @Singleton
  fun provideGoogleBillingClient(
    billingClient: BillingClient,
  ): GoogleBillingClient {
    return GoogleBillingClientImpl(billingClient)
  }

  @Provides
  @Singleton
  fun provideGoogleBillingService(
    googleBillingClient: GoogleBillingClient,
  ): GoogleBillingService {
    return GoogleBillingServiceImpl(googleBillingClient)
  }
}
