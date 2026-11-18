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
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)
        }

        androidMain.dependencies {
            implementation(libs.ktor.android)
            implementation(libs.androidx.core.core.ktx)
        }

        jvmMain.dependencies {
            implementation(libs.ktor.cio)
        }

        appleMain.dependencies {
            implementation(libs.ktor.darwin)
        }
    }
}

mavenPublishing {
    pom {
        name.set("JchuComponents Network")
        description.set(project.description)
    }
}
