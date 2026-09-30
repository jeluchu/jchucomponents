plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
    id("jchucomponents.publish")
}

version = libs.versions.jchucomponents.get()
description = "Dependency injection helpers for AndroidX Navigation 3."

android {
    namespace = "com.jeluchu.jchucomponents.navigation3.di"
    compileSdk = libs.versions.android.compile.sdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.min.sdk.get().toInt()
    }

    buildFeatures.compose = true

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    }

}

dependencies {
    api(project(":jchucomponents-navigation3"))
    api(libs.koin.androidx.compose)
}

mavenPublishing {
    pom {
        name.set("JchuComponents Navigation 3 DI")
        description.set(project.description)
    }
}
