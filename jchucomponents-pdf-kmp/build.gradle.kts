plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.jetbrains.dokka)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.serialization)
    id("jchucomponents.publish")
}

version = libs.versions.jchucomponents.get()
description = "Compose Multiplatform PDF reader for Android and iOS."

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    android {
        namespace = "com.jeluchu.jchucomponents.pdf.kmp"
        compileSdk = libs.versions.android.compile.sdk.get().toInt()
        minSdk = libs.versions.android.min.sdk.get().toInt()
        withSourcesJar(publish = true)
        withHostTest {}
    }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
        }

        commonMain.dependencies {
            api(project(":jchucomponents-navigation3"))
            api(compose.runtime)
            api(libs.org.jetbrains.kotlinx.kotlinx.serialization.json)
            implementation(project(":jchucomponents-network"))
            implementation(project(":jchucomponents-ui-kmp"))
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation(libs.org.jetbrains.kotlinx.kotlinx.coroutines.core)
        }

        androidMain.dependencies {
            implementation(libs.androidx.core.core.ktx)
        }
    }
}

compose.resources {
    packageOfResClass = "com.jeluchu.jchucomponents.pdf.kmp.resources"
}

dependencies {
    add("kspCommonMainMetadata", project(":jchucomponents-navigation3"))
}

tasks.matching { it.name == "runKtlintCheckOverCommonMainSourceSet" }.configureEach {
    dependsOn("kspCommonMainKotlinMetadata")
}

tasks.matching { it.name == "sourcesJar" || it.name.endsWith("SourcesJar") }.configureEach {
    dependsOn("kspCommonMainKotlinMetadata")
}

tasks.configureEach {
    if (name.startsWith("compile") && name != "compileKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

mavenPublishing {
    pom {
        name.set("JchuComponents PDF Reader Multiplatform")
        description.set(project.description)
    }
}
