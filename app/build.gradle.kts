import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.dokka)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
}

val localProperties =
    Properties().apply {
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use { input ->
                load(input)
            }
        }
    }

android {
    compileSdk =
        libs.versions.android.compile.sdk
            .get()
            .toInt()
    defaultConfig {
        applicationId = "com.jeluchu.composer"
        minSdk =
            libs.versions.sample.min.sdk
                .get()
                .toInt()
        targetSdk =
            libs.versions.android.target.sdk
                .get()
                .toInt()
        versionCode = 1
        versionName = libs.versions.jchucomponents.get()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures {
        buildConfig = true
        compose = true
    }
    defaultConfig {
        buildConfigField(
            "String",
            "SUPABASE_URL",
            "\"${localProperties.getProperty("supabase.url").orEmpty()}\""
        )
        buildConfigField(
            "String",
            "SUPABASE_PUBLISHABLE_KEY",
            "\"${localProperties.getProperty("supabase.publishableKey").orEmpty()}\""
        )
    }
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
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
    implementation(project(":jchucomponents-room"))
    implementation(project(":jchucomponents-supabase"))

    androidTestImplementation(project(":jchucomponents-room"))
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.runner)

    kspAndroidTest(libs.androidx.room3.compiler)
    ksp(libs.androidx.room3.compiler)
}
