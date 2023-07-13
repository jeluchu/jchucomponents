/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jetbrains.dokka")
}

android {

    compileSdk = 33
    defaultConfig {
        applicationId = "com.jeluchu.composer"
        minSdk = 22
        targetSdk = 33
        versionCode = 1
        versionName = "1.0.3"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = "1.4.7"
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    namespace = "com.jeluchu.composer"
}

dependencies {
    implementation(libs.bundles.ui.compose)
    implementation(project(":jchucomponents-core"))
    implementation(project(":jchucomponents-ui"))
    implementation(project(":jchucomponents-ktx"))
    implementation(project(":jchucomponents-qr"))
    implementation(project(":jchucomponents-pay"))
}