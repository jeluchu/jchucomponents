plugins {
    id("jchucomponents.kmp-library")
    id("jchucomponents.publish")
}

version = libs.versions.jchucomponents.get()
description = "Kotlin Multiplatform payment models and RevenueCat utilities."

kotlin {
    android {
        namespace = "com.jeluchu.jchucomponents.pay"
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.org.jetbrains.kotlinx.kotlinx.coroutines.core)
        }

        androidMain.dependencies {
            api(libs.revenuecat)
            implementation(libs.org.jetbrains.kotlinx.kotlinx.coroutines.android)
        }
    }
}

mavenPublishing {
    pom {
        name.set("JchuComponents Pay")
        description.set(project.description)
    }
}
