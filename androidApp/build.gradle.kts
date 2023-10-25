plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.khanalytic.kmm.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.khanalytic.kmm.android"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
//    kotlinOptions {
//        jvmTarget = "1.9.10"
//    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.ui:ui-tooling:1.5.4")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.4")
    implementation("androidx.compose.foundation:foundation:1.5.4")
    implementation("androidx.compose.material:material:1.5.4")
    implementation("androidx.activity:activity-compose:1.8.0")

    val flogger_version = "0.7.4"
    implementation("com.google.flogger:flogger:$flogger_version")
    implementation("com.google.flogger:flogger-system-backend:$flogger_version")
}