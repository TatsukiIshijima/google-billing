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
  namespace = "com.tatsuki.billing.feature"

  defaultConfig {
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
}

dependencies {
  implementation(project(":core"))
  testImplementation(project(":fake"))
  testImplementation(libs.junit)
  testImplementation(libs.androidx.fragment.ktx)
  testImplementation(libs.androidx.test.ext.junit.ktx)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.robolectric)
  testImplementation(libs.androidx.test.ext.junit)
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
