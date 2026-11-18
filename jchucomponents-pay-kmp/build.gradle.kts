plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.jetbrains.dokka)
    id("jchucomponents.publish")
}

version = libs.versions.jchucomponents.get()
description = "Compose Multiplatform RevenueCat payment integration for Android and iOS."

val swiftSimulatorLibsDir = runCatching {
    val process = ProcessBuilder("xcode-select", "-p").start()
    val developerDir = process.inputStream.bufferedReader().readText().trim()
    check(process.waitFor() == 0 && developerDir.isNotEmpty())
    "$developerDir/Toolchains/XcodeDefault.xctoolchain/usr/lib/swift/iphonesimulator"
}.getOrNull()

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
    val iosSimulatorArm64Target = iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            api(project(":jchucomponents-config"))
            api(libs.revenuecat.kmp.core)
            api(libs.org.jetbrains.kotlinx.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        all {
            if (name.startsWith("ios")) {
                languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
            }
        }
    }

    swiftSimulatorLibsDir?.let { swiftLibsDir ->
        iosSimulatorArm64Target.binaries.getTest("DEBUG").linkerOpts.addAll(
            listOf(
                "-L$swiftLibsDir",
                "-lswiftCompatibilityConcurrency",
                "-lswiftCompatibility56",
            ),
        )
    }
}

mavenPublishing {
    pom {
        name.set("JchuComponents Pay Multiplatform")
        description.set(project.description)
    }
}
