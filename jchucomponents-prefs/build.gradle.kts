plugins {
    id("jchucomponents.kmp-library")
    id("jchucomponents.publish")
}

version = libs.versions.jchucomponents.get()
description = "Kotlin Multiplatform DataStore preferences for Android and iOS."

kotlin {
    android {
        namespace = "com.jeluchu.jchucomponents.prefs"
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.androidx.datastore)
            implementation(libs.org.jetbrains.kotlinx.kotlinx.coroutines.core)
        }
    }
}

mavenPublishing {
    pom {
        name.set("JchuComponents Preferences")
        description.set(project.description)
    }
}
