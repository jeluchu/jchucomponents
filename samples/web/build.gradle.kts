import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName = "catalog"
        browser {
            commonWebpackConfig { outputFileName = "catalog.js" }
        }
        binaries.executable()
    }

    sourceSets {
        wasmJsMain {
            kotlin.srcDir(layout.buildDirectory.dir("generated/apiLinks"))
        }
        wasmJsMain.dependencies {
            implementation(project(":jchucomponents-ui"))
            implementation(project(":jchucomponents-foundation"))
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
        }
    }
}

compose {
    resources {
        packageOfResClass = "com.jeluchu.catalog.resources"
        generateResClass = always
    }
}

apply(from = "catalog-coverage.gradle.kts")

// Everything that reads the generated ApiLinks.kt must run after it is generated (ktlint already skips generated sources).
tasks.matching { it.name.startsWith("compileKotlin") || it.name.contains("Ktlint", ignoreCase = true) || it.name.endsWith("SourcesJar") }
    .configureEach { dependsOn("generateApiLinks") }
