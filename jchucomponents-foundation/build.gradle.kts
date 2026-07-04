import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    id("jchucomponents.kmp-library")
    id("jchucomponents.publish")
}

version = libs.versions.jchucomponents.get()
description = "Portable state, date, text and utility APIs for Kotlin Multiplatform."

kotlin {
    android {
        namespace = "com.jeluchu.jchucomponents.foundation"
    }

    val xcFramework = XCFramework(xcFrameworkName = "JchuComponentsCore")

    targets
        .withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>()
        .matching { it.konanTarget.family.isAppleFamily }
        .configureEach {
            binaries.framework {
                baseName = "JchuComponentsCore"
                binaryOption(
                    "bundleId",
                    "com.jeluchu.jchucomponents.core",
                )
                isStatic = true
                xcFramework.add(this)
            }
        }

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.datetime)
            api(libs.org.jetbrains.kotlinx.kotlinx.serialization.json)
        }
    }
}

mavenPublishing {
    pom {
        name.set("JchuComponents Foundation")
        description.set(project.description)
    }
}
