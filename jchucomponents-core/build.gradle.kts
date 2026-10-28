plugins {
    id("jchucomponents.kmp-library")
    id("jchucomponents.publish")
}

version = libs.versions.jchucomponents.get()
description = "Android architecture and lifecycle utilities with portable core APIs."

kotlin {
    android {
        namespace = "com.jeluchu.jchucomponents.core"
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.org.jetbrains.kotlinx.kotlinx.coroutines.core)
            implementation(project(":jchucomponents-foundation"))
        }

        androidMain.dependencies {
            api(project(":jchucomponents-network"))
            api(libs.bundles.core.androidx)
            api(libs.com.google.code.gson)
            implementation(libs.org.jetbrains.kotlinx.kotlinx.coroutines.android)
        }
    }
}

mavenPublishing {
    pom {
        name.set("JchuComponents Core")
        description.set(project.description)
    }
}
