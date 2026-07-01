plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.dokka)
    id("maven-publish")
}

android {
    namespace = "com.jeluchu.jchucomponents.core"
    compileSdk = 37

    defaultConfig {
        minSdk = 21
        proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }

}

dependencies {
    implementation(libs.bundles.core.androidx)
    implementation(libs.bundles.core.squareup)
    implementation(libs.bundles.core.jetbrains)
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.github.jeluchu"
            artifactId = "jchucomponents-core"
            version = libs.versions.jchucomponents.get()

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}
