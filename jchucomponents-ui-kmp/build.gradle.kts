plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.jetbrains.dokka)
    id("jchucomponents.publish")
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

version = libs.versions.jchucomponents.get()
description = "Kotlin Multiplatform Compose components for Android and iOS."

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    android {
        namespace = "com.jeluchu.jchucomponents.ui.kmp"
        compileSdk = libs.versions.android.compile.sdk.get().toInt()
        minSdk = libs.versions.android.min.sdk.get().toInt()
        withSourcesJar(publish = true)
        withHostTest {}
    }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            api(project(":jchucomponents-foundation"))
            api(compose.runtime)
            api(compose.ui)
            api(compose.animation)
            api(compose.foundation)
            api(compose.material)
            api(compose.material3)
            api(compose.materialIconsExtended)
            api(libs.org.jetbrains.kotlinx.kotlinx.coroutines.core)
            api(libs.kotlinx.datetime)
            implementation(libs.compose.ui.tooling.preview.kmp)
        }
    }
}

mavenPublishing {
    pom {
        name.set("JchuComponents UI Multiplatform")
        description.set(project.description)
    }
}
