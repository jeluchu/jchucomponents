plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.jetbrains.dokka)
    id("jchucomponents.publish")
}

version = libs.versions.jchucomponents.get()
description = "Compose Multiplatform RevenueCat payment integration for Android and iOS."

kotlin {
    android {
        namespace = "com.jeluchu.jchucomponents.pay.kmp"
        compileSdk =
            libs.versions.android.compile.sdk
                .get()
                .toInt()
        minSdk =
            libs.versions.android.min.sdk
                .get()
                .toInt()
        withSourcesJar(publish = true)
        withHostTest {}
    }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            api(libs.revenuecat.kmp.core)
            api(libs.org.jetbrains.kotlinx.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
        }

        all {
            if (name.startsWith("ios")) {
                languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
            }
        }
    }
}

mavenPublishing {
    pom {
        name.set("JchuComponents Pay Multiplatform")
        description.set(project.description)
    }
}
