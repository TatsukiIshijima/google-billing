import java.io.FileInputStream
import java.util.Properties

plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  id("maven-publish")
}

val libVersionsPropertiesFile = rootProject.file("libversions.properties")
val libVersionsProperties = Properties()
libVersionsProperties.load(FileInputStream(libVersionsPropertiesFile))

android {
  namespace = "com.tatsuki.billing.fake"
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
  testOptions {
    unitTests {
      isReturnDefaultValues = true
    }
  }
}

dependencies {
//  implementation("com.github.TatsukiIshijima:google-billing:core:change-multi-module-SNAPSHOT")
  implementation(project(":core"))
  implementation("com.google.code.gson:gson:2.10.1")
  implementation("androidx.test.ext:junit-ktx:1.1.5")
  testImplementation("org.json:json:20230618")
  testImplementation("junit:junit:4.13.2")
  testImplementation("org.robolectric:robolectric:4.9")
}

publishing {
  publications {
    register<MavenPublication>("release") {
      version = libVersionsProperties["VERSION_NAME"] as String

      afterEvaluate {
        from(components["release"])
      }
    }
  }
}
