plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.jetbrains.dokka)
    alias(libs.plugins.compose.compiler)
    id("jchucomponents.publish")
}

version = libs.versions.jchucomponents.get()
description = "Dependency injection helpers for Navigation 3 ViewModels."

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())

    android {
        namespace = "com.jeluchu.jchucomponents.navigation3.di"
        compileSdk = libs.versions.android.compile.sdk.get().toInt()
        minSdk = libs.versions.android.min.sdk.get().toInt()
        withSourcesJar(publish = true)
    }

    jvm()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            api(project(":jchucomponents-navigation3"))
            api(libs.koin.compose.multiplatform)
            api(libs.koin.compose.viewmodel.multiplatform)
        }
    }
}

mavenPublishing {
    pom {
        name.set("JchuComponents Navigation 3 DI")
        description.set(project.description)
    }
}
