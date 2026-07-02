plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.dokka)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    compileSdk = 37
    defaultConfig {
        applicationId = "com.jeluchu.composer"
        minSdk = 23
        targetSdk = 37
        versionCode = 1
        versionName = libs.versions.jchucomponents.get()
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    
    buildFeatures.compose = true
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
        isCoreLibraryDesugaringEnabled = true
    }

    namespace = "com.jeluchu.composer"
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose.bom)
    implementation(libs.androidx.appcompat)
    implementation(libs.bundles.navigation3)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(project(":jchucomponents-core"))
    implementation(project(":jchucomponents-foundation"))
    implementation(project(":jchucomponents-ui"))
    implementation(project(":jchucomponents-ktx"))
    implementation(project(":jchucomponents-qr"))
    implementation(project(":jchucomponents-pay"))
}
