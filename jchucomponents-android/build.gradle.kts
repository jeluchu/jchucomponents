plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.dokka)
    id("jchucomponents.publish")
}

version = libs.versions.jchucomponents.get()
description = "Android utilities: Context and lifecycle extensions, ViewModel base, media player and Google Play in-app updates."

android {
    namespace = "com.jeluchu.jchucomponents.android"
    compileSdk {
        version =
            release(libs.versions.android.compile.sdk.get().toInt()) {
                minorApiLevel = libs.versions.android.compile.minor.api.get().toInt()
            }
    }

    defaultConfig {
        minSdk = libs.versions.android.min.sdk.get().toInt()
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    }
}

dependencies {
    api(project(":jchucomponents-foundation"))
    api(project(":jchucomponents-network"))
    api(libs.org.jetbrains.kotlinx.kotlinx.coroutines.core)
    api(libs.org.jetbrains.kotlinx.kotlinx.serialization.json)
    api(libs.bundles.core.androidx)
    api(libs.com.google.code.gson)
    api(libs.androidx.activity)
    api(libs.androidx.lifecycle.runtime)

    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.ktx.google)
    implementation(libs.bundles.ktx.androidx)
    implementation(libs.org.jetbrains.kotlinx.kotlinx.coroutines.android)
    implementation(libs.play.app.update)
}

mavenPublishing {
    pom {
        name.set("JchuComponents Android")
        description.set(project.description)
    }
}
