package com.tatsuki.inappbilling.module

import android.content.Context
import com.tatsuki.billing.core.GoogleBillingClientFactory
import com.tatsuki.billing.feature.GoogleBillingClientFactoryImpl
import com.tatsuki.billing.feature.GoogleBillingService
import com.tatsuki.billing.feature.GoogleBillingServiceImpl
import com.tatsuki.billing.feature.listener.ConnectionStateListener
import com.tatsuki.billing.feature.listener.PurchasesListener
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
  fun provideGoogleBillingService(
    googleBillingClientFactory: GoogleBillingClientFactory,
    connectionStateListener: ConnectionStateListener,
    purchasesListener: PurchasesListener,
  ): GoogleBillingService {
    return GoogleBillingServiceImpl(
      googleBillingFactory = googleBillingClientFactory,
      connectionStateListener = connectionStateListener,
      purchasesListener = purchasesListener
    )
  }

  @Provides
  @Singleton
  fun provideGoogleBillingClientFactory(
    @ApplicationContext context: Context,
  ): GoogleBillingClientFactory {
    return GoogleBillingClientFactoryImpl(context)
  }
}

@Module
@InstallIn(SingletonComponent::class)
object BillingClientListenerModule {

  @Provides
  @Singleton
  fun provideConnectionListener(): ConnectionStateListener {
    return ConnectionStateListener
  }

  @Provides
  @Singleton
  fun providePurchasesListener(): PurchasesListener {
    return PurchasesListener
  }
}
