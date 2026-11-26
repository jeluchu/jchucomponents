import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.jetbrains.dokka)
    id("jchucomponents.publish")
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

version = libs.versions.jchucomponents.get()
description = "Compose Multiplatform components for Android, iOS and JVM desktop, with Android-specific extras."

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    android {
        namespace = "com.jeluchu.jchucomponents.ui"
        compileSdk {
            version =
                release(
                    libs.versions.android.compile.sdk
                        .get()
                        .toInt()
                ) {
                    minorApiLevel =
                        libs.versions.android.compile.minor.api
                            .get()
                            .toInt()
                }
        }
        minSdk =
            libs.versions.android.min.sdk
                .get()
                .toInt()
        withSourcesJar(publish = true)
        androidResources.enable = true
        withHostTest {}
        withDeviceTest {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            // Browser tests need Chrome, which not every machine has; the Node target runs the same tests.
            testTask { enabled = false }
        }
        nodejs()
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
            api(compose.components.resources)
            // Same Material 3 as the pdf reader (it needs LoadingIndicator); apps get it transitively.
            api(libs.compose.material3.expressive)
            api(compose.materialIconsExtended)
            api(libs.org.jetbrains.kotlinx.kotlinx.coroutines.core)
            api(libs.coil.compose)
            api(libs.coil.compose.ktor)
            api(libs.kotlinx.datetime)
            implementation(libs.compose.ui.tooling.preview.kmp)
        }

        androidMain.dependencies {
            implementation(project.dependencies.platform(libs.compose.bom))
            implementation(libs.bundles.compose.bom)
            implementation(libs.androidx.compose.animation.graphics)
            implementation(libs.bundles.ui.androidx)
            implementation(project(":jchucomponents-android"))
        }

        getByName("androidHostTest").dependencies {
            implementation(libs.junit)
        }

        getByName("androidDeviceTest").dependencies {
            implementation(project.dependencies.platform(libs.compose.bom))
            implementation(libs.androidx.compose.ui.test.junit4)
            implementation(libs.androidx.compose.ui.test.manifest)
            implementation(libs.androidx.test.ext.junit)
            implementation(libs.androidx.test.espresso.core)
            implementation(libs.androidx.test.runner)
        }
    }
}

mavenPublishing {
    pom {
        name.set("JchuComponents UI Multiplatform")
        description.set(project.description)
    }
}

// ui is a library and has no Compose UI tests for Wasm, so it declares no executable binary; skip the check that asks for one.
tasks.matching { it.name == "checkComposeUiTestConfigurationForWasmJs" }.configureEach { enabled = false }
