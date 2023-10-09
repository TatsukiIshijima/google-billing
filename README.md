# google-billing

[![](https://jitpack.io/v/TatsukiIshijima/google-billing.svg)](https://jitpack.io/#TatsukiIshijima/google-billing)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## What's this?
This library is Google Play Billing Library wrapper with Coroutine.

## Add dependencies

1. Add it in your settings.gradle at the end of repositories.

```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

2. Add the dependency

 ```
dependencies {
    implementation("com.github.TatsukiIshijima.google-billing:core:$version")
    implementation("com.github.TatsukiIshijima.google-billing:feature:$version")
    // option
    // testImplementation("com.github.TatsukiIshijima.google-billing:fake:$version")
}
 ```

## How to Use
### instance

 ```kotlin
val googleBillingService = GoogleBillingServiceImpl(
  GoogleBillingClientFactoryImpl(
    context,
    PurchasesListener
  )
)
```

### connect

```kotlin
coroutineScope.launch {
  try {
    val connectionState = googleBillingService.connect()
  } catch (e: GoogleBillingServiceException) {
    
  }
}
```

### disconnect

```kotlin
googleBillingService.disconnect()
```

### purchaseSubscription

```kotlin
coroutineScope.launch {
  try {
    googleBillingService.purchaseSubscription(
      productDetails,
      offerToken,
      activity,
    )
  } catch (e: GoogleBillingServiceException) {

  }
}
```

### acknowledgePurchase

```kotlin
coroutineScope.launch {
  try {
    googleBillingService.acknowledgePurchase(purchaseToken)
  } catch (e: GoogleBillingServiceException) {

  }
}
```

### purchaseConsumableProduct

```kotlin
coroutineScope.launch {
  try {
    googleBillingService.purchaseConsumableProduct(
      productDetails,
      activity
    )
  } catch (e: GoogleBillingServiceException) {

  }
}
```

### consumePurchase

```kotlin
coroutineScope.launch {
  try {
    googleBillingService.consumePurchase(purchaseTOken)
  } catch (e: GoogleBillingServiceException) {

  }
}
```

## Local build
To build in a local environment, execute the following command in project directory. If successful, an artifact for each module will be created in $HOME/.m2/google-billing.

### core
```shell
./gradlew :core:publishToMavenLocal
```

### feature
```shell
./gradlew :feature:publishToMavenLocal
```

### fake
```shell
./gradlew :fake:publishToMavenLocal
```
