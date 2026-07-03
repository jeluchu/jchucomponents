plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.jetbrains.dokka)
    id("maven-publish")
}

group = "com.github.jeluchu"
version = libs.versions.jchucomponents.get()

kotlin {
    android {
        namespace = "com.jeluchu.jchucomponents.pay"
        compileSdk = libs.versions.android.compile.sdk.get().toInt()
        minSdk = libs.versions.android.min.sdk.get().toInt()

        withSourcesJar(publish = true)
        withHostTest {}
    }

    iosArm64()
    iosX64()
    iosSimulatorArm64()

    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        compilerOptions {
            freeCompilerArgs.add("-Xexpect-actual-classes")
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.org.jetbrains.kotlinx.kotlinx.coroutines.core)
        }

        androidMain.dependencies {
            implementation(libs.revenuecat)
            implementation(libs.org.jetbrains.kotlinx.kotlinx.coroutines.android)
        }

        commonTest.dependencies {
            implementation(kotlin(simpleModuleName = "test"))
        }
    }
}
