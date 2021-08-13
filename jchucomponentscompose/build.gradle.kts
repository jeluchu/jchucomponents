plugins {
    id("com.android.library")
    kotlin("android")
}

android {

    compileSdk = 30
    defaultConfig {
        minSdk = 22
        targetSdk = 30
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.0.1"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    lint {
        isWarningsAsErrors = false
        isAbortOnError = false
    }

    packagingOptions {
        resources.excludes.add("AndroidManifest.xml")
        resources.excludes.add("LICENSE.txt")
        resources.excludes.add("META-INF/DEPENDENCIES")
        resources.excludes.add("META-INF/ASL2.0")
        resources.excludes.add("META-INF/NOTICE")
        resources.excludes.add("META-INF/LICENSE")
        resources.excludes.add("*.kotlin_module")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

}

dependencies {

    // KOTLIN LIBRARY ------------------------------------------------------------------------------
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.1")

    // JETPACK COMPOSE -----------------------------------------------------------------------------
    implementation("androidx.compose.ui:ui:1.0.1")
    implementation("androidx.compose.material:material:1.0.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.0.1")
    implementation("androidx.activity:activity-compose:1.3.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.0.1")
    implementation("androidx.activity:activity-compose:1.3.1")
    implementation("androidx.compose.material:material-icons-extended:1.0.1")
    implementation("androidx.compose.foundation:foundation:1.0.1")
    implementation("androidx.compose.foundation:foundation-layout:1.0.1")
    implementation("androidx.compose.animation:animation:1.0.1")
    implementation("androidx.compose.runtime:runtime:1.0.1")
    implementation("androidx.compose.runtime:runtime-livedata:1.0.1")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0-beta02")
    implementation("androidx.navigation:navigation-compose:2.4.0-alpha06")
    implementation("androidx.activity:activity-compose:1.3.1")

    // ACCOMPANIST GOOGLE LIBRARY ------------------------------------------------------------------
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.15.0")
    implementation("com.google.accompanist:accompanist-navigation-animation:0.16.1")

    // ANDROIDX LIBRARY ----------------------------------------------------------------------------
    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.browser:browser:1.3.0")
    implementation("androidx.preference:preference-ktx:1.1.1")

    // GOOGLE LIBRARY ------------------------------------------------------------------------------
    implementation("com.google.android.material:material:1.4.0")
    implementation("com.google.code.gson:gson:2.8.7")

    // SQUAREUP LIBRARY ----------------------------------------------------------------------------
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    // THIRD PARTY DEPENDENCIES --------------------------------------------------------------------
    implementation("io.coil-kt:coil-compose:1.3.1")

}