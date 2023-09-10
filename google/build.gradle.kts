plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  id("maven-publish")
}

android {
  namespace = "com.tatsuki.google"
  compileSdk = 34

  defaultConfig {
    minSdk = 21
    targetSdk = 34

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

dependencies {
  api("com.android.billingclient:billing:6.0.1")
  api("com.android.billingclient:billing-ktx:6.0.1")
  testImplementation("junit:junit:4.13.2")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
}

publishing {
  publications {
    register<MavenPublication>("release") {
      groupId = "com.github.TatsukiIshijima"
      artifactId = "google-billing"
      version = "0.0.3"

      afterEvaluate {
        from(components["release"])
      }
    }
  }
}
