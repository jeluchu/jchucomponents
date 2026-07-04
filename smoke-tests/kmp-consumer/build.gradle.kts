plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.android.kmp.library)
}

kotlin {
    android {
        namespace = "com.jeluchu.jchucomponents.smoke"
        compileSdk = libs.versions.android.compile.sdk.get().toInt()
        minSdk = libs.versions.android.min.sdk.get().toInt()
    }

    iosArm64()
    iosX64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            val jchuComponentsVersion = libs.versions.jchucomponents.get()

            implementation(
                "io.github.jeluchu:jchucomponents-foundation:$jchuComponentsVersion"
            )
            implementation(
                "io.github.jeluchu:jchucomponents-network:$jchuComponentsVersion"
            )
            implementation(
                "io.github.jeluchu:jchucomponents-pay:$jchuComponentsVersion"
            )
            implementation(
                "io.github.jeluchu:jchucomponents-prefs:$jchuComponentsVersion"
            )
            implementation(
                "io.github.jeluchu:jchucomponents-qr:$jchuComponentsVersion"
            )
        }
    }
}
