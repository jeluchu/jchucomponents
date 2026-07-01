import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.android.kmp.library)
    id("maven-publish")
}

group = "com.github.jeluchu"
version = libs.versions.jchucomponents.get()

kotlin {
    android {
        namespace = "com.jeluchu.jchucomponents.foundation"
        compileSdk = 37
        minSdk = 21

        withSourcesJar(publish = true)
        withHostTest {}
    }

    val xcFramework = XCFramework(xcFrameworkName = "JchuComponentsCore")

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "JchuComponentsCore"
            binaryOption(
                "bundleId",
                "com.jeluchu.jchucomponents.core"
            )
            isStatic = true
            xcFramework.add(this)
        }
    }

    sourceSets {
        commonTest.dependencies {
            implementation(kotlin(simpleModuleName = "test"))
        }
    }
}
