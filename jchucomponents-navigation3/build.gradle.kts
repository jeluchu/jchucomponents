plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.jetbrains.dokka)
    alias(libs.plugins.compose.compiler)
    id("jchucomponents.publish")
}

version = libs.versions.jchucomponents.get()
description = "AndroidX Navigation 3 runtime, KSP code generation and Koin ViewModel injection helpers."

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())

    android {
        namespace = "com.jeluchu.jchucomponents.navigation3"
        compileSdk {
            version =
                release(libs.versions.android.compile.sdk.get().toInt()) {
                    minorApiLevel = libs.versions.android.compile.minor.api.get().toInt()
                }
        }
        minSdk = libs.versions.android.min.sdk.get().toInt()
        withSourcesJar(publish = true)
    }

    jvm()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            api(libs.androidx.navigation3.runtime)
            api(libs.koin.compose.multiplatform)
            api(libs.koin.compose.viewmodel.multiplatform)
        }

        jvmMain.dependencies {
            implementation(libs.symbol.processing.api)
        }
    }
}

mavenPublishing {
    pom {
        name.set("JchuComponents Navigation 3")
        description.set(project.description)
    }
}
