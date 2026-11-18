plugins {
    id("jchucomponents.kmp-library")
    id("jchucomponents.publish")
}

version = libs.versions.jchucomponents.get()
description = "Single entry point (JchuConfig) to configure JchuComponents modules and Koin on Android, iOS, macOS and JVM desktop."

kotlin {
    android {
        namespace = "com.jeluchu.jchucomponents.config"
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            api(libs.koin.core)
        }

        androidMain.dependencies {
            api(libs.koin.android)
        }
    }
}

mavenPublishing {
    pom {
        name.set("JchuComponents Config")
        description.set(project.description)
    }
}
