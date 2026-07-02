plugins {
    alias(libs.plugins.jetbrains.dokka) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.kmp.library) apply false
    alias(libs.plugins.jetbrains.kotlin.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.binary.compatibility.validator)
}

apiValidation {
    ignoredProjects += listOf("app")
    ignoredPackages += listOf(
        "com.jeluchu.composer",
        "com.jeluchu.jchucomponents.ui.accompanist"
    )
}
