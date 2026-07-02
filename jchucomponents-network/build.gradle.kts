plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.android.kmp.library)
    id("maven-publish")
}

group = "com.github.jeluchu"
version = libs.versions.jchucomponents.get()

kotlin {
    android {
        namespace = "com.jeluchu.jchucomponents.network"
        compileSdk = 37
        minSdk = 21

        withSourcesJar(publish = true)
        withHostTest {}
    }

    iosArm64()
    iosX64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            api(libs.ktor.client.core)
        }

        commonTest.dependencies {
            implementation(kotlin(simpleModuleName = "test"))
        }
    }
}
