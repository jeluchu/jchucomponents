plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.dokka)
    id("jchucomponents.publish")
}

version = libs.versions.jchucomponents.get()
description = "Android Google Play in-app update state and lifecycle handling."

android {
    namespace = "com.jeluchu.jchucomponents.appupdates"
    compileSdk = libs.versions.android.compile.sdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.min.sdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    }
}

dependencies {
    api(libs.androidx.activity)
    api(libs.androidx.lifecycle.runtime)
    api(libs.org.jetbrains.kotlinx.kotlinx.coroutines.core)
    implementation(libs.play.app.update)
}

mavenPublishing {
    pom {
        name.set("JchuComponents App Updates")
        description.set(project.description)
    }
}
