import java.io.FileInputStream
import java.util.Properties

plugins {
  id("com.android.application")
  id("com.github.triplet.play") version "3.9.1"
  id("com.google.dagger.hilt.android")
  id("org.jetbrains.kotlin.android")
  kotlin("kapt")
}

// If release build, please disable comment out.
//val keystorePropertiesFile = rootProject.file("local.properties")
//val keystoreProperties = Properties()
//keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
  namespace = "com.tatsuki.inappbilling"
  compileSdk = 34

  // If release build, please disable comment out.
//  signingConfigs {
//    create("release") {
//      keyAlias = keystoreProperties["keyAlias"] as String
//      keyPassword = keystoreProperties["keyPassword"] as String
//      storeFile = file(keystoreProperties["storeFile"] as String)
//      storePassword = keystoreProperties["storePassword"] as String
//    }
//  }

  defaultConfig {
    applicationId = "com.tatsuki.inappbilling"
    minSdk = 21
    targetSdk = 34
    versionCode = 1
    versionName = "0.1"

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

      // If release build, please disable comment out.
//      signingConfig = signingConfigs.getByName("release")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>()
    .configureEach {
      kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
      }
    }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.4.6"
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
  implementation("androidx.core:core-ktx:1.10.1")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
  implementation("androidx.activity:activity-compose:1.7.2")
  implementation(platform("androidx.compose:compose-bom:2022.10.00"))
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.ui:ui-graphics")
  implementation("androidx.compose.ui:ui-tooling-preview")
  implementation("androidx.compose.material3:material3")
  implementation("androidx.navigation:navigation-compose:2.7.4")
  implementation("com.google.dagger:hilt-android:2.45")
  kapt("com.google.dagger:hilt-android-compiler:2.45")
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
  androidTestImplementation(platform("androidx.compose:compose-bom:2022.10.00"))
  androidTestImplementation("androidx.compose.ui:ui-test-junit4")
  debugImplementation("androidx.compose.ui:ui-tooling")
  debugImplementation("androidx.compose.ui:ui-test-manifest")
}

kapt {
  correctErrorTypes = true
}
