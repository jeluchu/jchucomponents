plugins {
    id("jchucomponents.room-kmp-library")
    id("jchucomponents.publish")
}

version = libs.versions.jchucomponents.get()
description = "Kotlin Multiplatform Room 3 infrastructure for Android, Apple platforms and JVM desktop."

kotlin {
    android {
        namespace = "com.jeluchu.jchucomponents.room"
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            api(libs.androidx.room3.runtime)
            api(libs.androidx.sqlite.bundled)
            api(libs.org.jetbrains.kotlinx.kotlinx.coroutines.core)
        }
    }
}

mavenPublishing {
    pom {
        name.set("JchuComponents Room")
        description.set(project.description)
    }
}
