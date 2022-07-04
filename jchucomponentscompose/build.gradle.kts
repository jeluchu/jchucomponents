/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

plugins {
    id("com.android.library")
    kotlin("android")
    id("maven-publish")
    id("org.jetbrains.dokka")
}

android {

    compileSdk = 32
    buildToolsVersion = "30.0.3"
    defaultConfig {
        minSdk = 21
        targetSdk = 32
        consumerProguardFiles.add(file("consumer-rules.pro"))
    }
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = "1.2.0"

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    namespace = "com.jeluchu.jchucomponentscompose"

    tasks.dokkaHtml.configure {

        outputDirectory.set(file("../docs"))
        moduleName.set(rootProject.name)
        pluginsMapConfiguration.set(mapOf("org.jetbrains.dokka.base.DokkaBase" to """{ "customStyleSheets": ["${file("../logo-style.css")}"], "footerMessage": "Made with ❤️ at <b><a href=\"https://about.jeluchu.com\">Jéluchu</a></b> © 2022" }"""))

        dokkaSourceSets {

            named("main") {
                includes.from("../modules.md")
                skipDeprecated.set(false)
                displayName.set("JVM")
                platform.set(org.jetbrains.dokka.Platform.jvm)
                skipEmptyPackages.set(true)
                reportUndocumented.set(true)
                includeNonPublic.set(false)
                skipDeprecated.set(false)
                reportUndocumented.set(false)
            }

        }

    }

}

dependencies {

    // KOTLIN LIBRARY ------------------------------------------------------------------------------
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.0")
    implementation("androidx.core:core-ktx:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.3")

    // JETPACK COMPOSE -----------------------------------------------------------------------------
    implementation("androidx.compose.ui:ui:1.2.0-rc02")
    implementation("androidx.compose.material:material:1.2.0-rc02")
    implementation("androidx.compose.ui:ui-tooling-preview:1.2.0-rc02")
    implementation("androidx.activity:activity-compose:1.5.0")
    implementation("androidx.core:core-splashscreen:1.0.0-rc01")
    implementation("com.google.android.gms:play-services-base:18.1.0")
    debugImplementation("androidx.compose.ui:ui-tooling:1.2.0-rc02")
    implementation("androidx.compose.material:material-icons-extended:1.2.0-rc02")
    implementation("androidx.compose.foundation:foundation:1.2.0-rc02")
    implementation("androidx.compose.foundation:foundation-layout:1.2.0-rc02")
    implementation("androidx.compose.animation:animation:1.2.0-rc02")
    implementation("androidx.compose.runtime:runtime:1.2.0-rc02")
    implementation("androidx.compose.runtime:runtime-livedata:1.2.0-rc02")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.navigation:navigation-compose:2.5.0")

    // FIREBASE LIBRARY ----------------------------------------------------------------------------
    implementation("com.google.firebase:firebase-analytics-ktx:21.0.0")

    // ACCOMPANIST GOOGLE LIBRARY ------------------------------------------------------------------
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.23.1")
    implementation("com.google.accompanist:accompanist-navigation-animation:0.23.1")

    // ANDROIDX LIBRARY ----------------------------------------------------------------------------
    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.browser:browser:1.4.0")
    implementation("androidx.preference:preference-ktx:1.2.0")

    // GOOGLE LIBRARY ------------------------------------------------------------------------------
    implementation("com.google.code.gson:gson:2.9.0")

    // SQUAREUP LIBRARY ----------------------------------------------------------------------------
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // THIRD PARTY DEPENDENCIES --------------------------------------------------------------------
    implementation("io.coil-kt:coil-compose:2.1.0")

}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.jeluchu"
                artifactId = "jchucomponentscompose"
                version = "1.0.0-beta09"
            }
        }
    }
}