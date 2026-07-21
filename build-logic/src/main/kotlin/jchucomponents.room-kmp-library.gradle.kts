import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
    id("org.jetbrains.dokka")
}

val libsCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    android {
        compileSdk = libsCatalog.findVersion("android-compile-sdk")
            .get()
            .requiredVersion
            .toInt()
        minSdk = libsCatalog.findVersion("android-min-sdk")
            .get()
            .requiredVersion
            .toInt()

        withSourcesJar(publish = true)
        withHostTest {}
    }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
