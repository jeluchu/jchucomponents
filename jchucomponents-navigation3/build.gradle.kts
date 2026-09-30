plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.jetbrains.dokka)
    id("jchucomponents.publish")
}

version = libs.versions.jchucomponents.get()
description = "AndroidX Navigation 3 runtime and KSP code generation."

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())

    android {
        namespace = "com.jeluchu.jchucomponents.navigation3"
        compileSdk = libs.versions.android.compile.sdk.get().toInt()
        minSdk = libs.versions.android.min.sdk.get().toInt()
        withSourcesJar(publish = true)
    }

    jvm()

    sourceSets {
        androidMain.dependencies {
            api(libs.androidx.navigation3.runtime)
        }

        jvmMain.dependencies {
            implementation(libs.symbol.processing.api)
        }
    }
}

mavenPublishing {
    pom {
        name.set("JchuComponents Navigation 3")
        description.set(project.description)
    }
}
