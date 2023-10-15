import java.io.FileInputStream
import java.util.Properties

// FIXME: This is a workaround
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin)
  alias(libs.plugins.dagger.hilt)
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

dependencies {

  implementation(project(":core"))
  implementation(project(":feature"))
//  implementation("com.github.TatsukiIshijima.google-billing:core:$version")
//  implementation("com.github.TatsukiIshijima.google-billing:feature:$version")

  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.compose.bom)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.navigation.navigation.compose)
  implementation(libs.dagger.hilt)
  kapt(libs.dagger.hilt.compiler)

  testImplementation(libs.junit)

  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.androidx.test.espresso.core)
  androidTestImplementation(libs.androidx.compose.bom)
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)

  debugImplementation(libs.androidx.compose.ui.tooling)
  debugImplementation(libs.androidx.compose.ui.test.manifest)
}

kapt {
  correctErrorTypes = true
}
