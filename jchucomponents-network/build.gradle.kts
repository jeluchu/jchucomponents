plugins {
    id("jchucomponents.kmp-library")
    id("jchucomponents.publish")
}

version = libs.versions.jchucomponents.get()
description = "Kotlin Multiplatform networking utilities for Android, iOS, macOS and JVM desktop."

kotlin {
    android {
        namespace = "com.jeluchu.jchucomponents.network"
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            api(project(":jchucomponents-config"))
            api(libs.ktor.client.core)
            api(libs.org.jetbrains.kotlinx.kotlinx.coroutines.core)
            api(libs.ktor.client.content.negotiation)
            api(libs.ktor.client.logging)
            api(libs.ktor.serialization.kotlinx.json)
        }

        commonTest.dependencies {
            implementation(libs.ktor.client.mock)
        }

        androidMain.dependencies {
            api(libs.ktor.android)
            // Optional engine (see JchuHttpEngine.Cio), also used by the JVM target.
            api(libs.ktor.cio)
            implementation(libs.androidx.core.core.ktx)
        }

        jvmMain.dependencies {
            api(libs.ktor.cio)
        }

        appleMain.dependencies {
            api(libs.ktor.darwin)
        }
    }
}

mavenPublishing {
    pom {
        name.set("JchuComponents Network")
        description.set(project.description)
    }
}
