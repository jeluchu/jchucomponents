import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("jchucomponents.kmp-library")
    id("jchucomponents.publish")
}

version = libs.versions.jchucomponents.get()
description = "Unified crash reporting and analytics (Firebase Crashlytics and Analytics) for Android, iOS, macOS and JVM desktop."

kotlin {
    targets.withType<KotlinNativeTarget>().configureEach {
        compilations.getByName("main").cinterops.create("jchuObjc") {
            definitionFile.set(project.file("src/nativeInterop/cinterop/jchuObjc.def"))
        }
    }

    android {
        namespace = "com.jeluchu.jchucomponents.monitoring"
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            api(project(":jchucomponents-config"))
            api(libs.org.jetbrains.kotlinx.kotlinx.coroutines.core)
        }

        // Firebase is compile-only: the app owns the SDK and its version, the library never forces it.
        androidMain.dependencies {
            compileOnly(project.dependencies.platform(libs.firebase.bom))
            compileOnly(libs.firebase.crashlytics)
            compileOnly(libs.firebase.analytics)
        }
    }
}

mavenPublishing {
    pom {
        name.set("JchuComponents Monitoring")
        description.set(project.description)
    }
}
