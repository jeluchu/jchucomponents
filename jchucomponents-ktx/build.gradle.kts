plugins {
    id("jchucomponents.kmp-library")
    id("jchucomponents.publish")
}

version = libs.versions.jchucomponents.get()
description = "Android extensions with a small Kotlin Multiplatform compatibility API."

kotlin {
    android {
        namespace = "com.jeluchu.jchucomponents.ktx"
        androidResources.enable = true
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":jchucomponents-foundation"))
            api(libs.org.jetbrains.kotlinx.kotlinx.coroutines.core)
        }

        androidMain.dependencies {
            api(project(":jchucomponents-network"))
            api(libs.org.jetbrains.kotlinx.kotlinx.serialization.json)
            implementation(project.dependencies.platform(libs.compose.bom))
            implementation(libs.bundles.ktx.google)
            implementation(libs.bundles.ktx.androidx)
        }
    }
}

mavenPublishing {
    pom {
        name.set("JchuComponents KTX")
        description.set(project.description)
    }
}
