import java.io.FileInputStream
import java.util.Properties

// FIXME: This is a workaround
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin)
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
  implementation(project(":core"))
  implementation(libs.gson)
  testImplementation(libs.json)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.test.ext.junit)
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