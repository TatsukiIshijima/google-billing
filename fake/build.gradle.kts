import java.io.FileInputStream
import java.util.Properties

// FIXME: This is a workaround
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.inappbilling.android.library)
  id("maven-publish")
}

val libVersionsPropertiesFile = rootProject.file("libversions.properties")
val libVersionsProperties = Properties()
libVersionsProperties.load(FileInputStream(libVersionsPropertiesFile))

android {
  namespace = "com.tatsuki.billing.fake"

  defaultConfig {
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
  testImplementation(libs.androidx.test.ext.junit.ktx)
  testImplementation(libs.json)
  testImplementation(libs.junit)
  testImplementation(libs.robolectric)
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