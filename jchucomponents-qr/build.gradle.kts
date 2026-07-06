plugins {
    id("jchucomponents.kmp-library")
    id("jchucomponents.publish")
}

version = libs.versions.jchucomponents.get()
description = "Kotlin Multiplatform QR utilities for Android, iOS and macOS."

kotlin {
    android {
        namespace = "com.jeluchu.jchucomponents.qr"
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.core.core.ktx)
        }
    }
}

mavenPublishing {
    pom {
        name.set("JchuComponents QR")
        description.set(project.description)
    }
}
