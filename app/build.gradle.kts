plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.simonanger.gigmatcher"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.simonanger.gigmatcher"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14" // Make sure this matches the Compose version
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    packaging {
        resources {
            excludes += setOf(
                "META-INF/versions/9/OSGI-INF/MANIFEST.MF",
                "/META-INF/{AL2.0,LGPL2.1}",       // Keep your original excludes too
            )
        }
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2023.10.01") // Compose BOM

    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Navigation Compose (already included in libs, but you also have explicit version below)
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // Core Compose & Material3
    implementation("androidx.compose.material3:material3:1.1.0")
    implementation("androidx.compose.material3:material3-window-size-class:1.1.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.foundation:foundation")
    implementation("com.google.android.material:material:1.12.0")

    // Other
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.2")

    // Debug tools
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Your other libs references
    implementation(libs.androidx.navigation.compose.android)
    implementation(libs.identity.doctypes.jvm)
}
