import java.io.FileInputStream
import java.util.Properties

// FIXME: This is a workaround
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  id("com.github.triplet.play") version "3.9.1"
  alias(libs.plugins.inappbilling.android.application)
  alias(libs.plugins.inappbilling.android.application.compose)
  alias(libs.plugins.inappbilling.android.hilt)
}

android {
  namespace = "com.tatsuki.inappbilling"

  signingConfigs {
    create("release") {
      // If local build, create keystore.properties in the root of the project.
      // And the store file path, store password, keyAlias, and keyPassword.
      val keystorePropertiesFile = rootProject.file("keystore.properties")
      if (keystorePropertiesFile.exists()) {
        val keystoreProperties = Properties()
        keystoreProperties.load(FileInputStream(keystorePropertiesFile))
        storeFile = file(keystoreProperties["storeFile"] as String)
        storePassword = keystoreProperties["storePassword"] as String
        keyAlias = keystoreProperties["keyAlias"] as String
        keyPassword = keystoreProperties["keyPassword"] as String
      }
    }
  }

  defaultConfig {
    applicationId = "com.tatsuki.inappbilling"
    versionCode = 2
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }

    setProperty("archivesBaseName", "${applicationId}-${versionName}(${versionCode})")
  }

  buildTypes {
    debug {
      isDebuggable = true
    }
    release {
      isDebuggable = false
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

      signingConfig = signingConfigs.getByName("release")
    }
  }
  packagingOptions {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

play {
    serviceAccountCredentials.set(rootProject.file("google-billing-service-account-key.json"))
}

dependencies {

  implementation(project(":core"))
  implementation(project(":feature"))
//  implementation("com.github.TatsukiIshijima.google-billing:core:$version")
//  implementation("com.github.TatsukiIshijima.google-billing:feature:$version")

  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.navigation.navigation.compose)

  testImplementation(libs.junit)

  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.androidx.test.espresso.core)
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)

  debugImplementation(libs.androidx.compose.ui.test.manifest)
}
