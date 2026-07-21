plugins {
    id("jchucomponents.lint") apply false
    alias(libs.plugins.jetbrains.dokka)
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.kmp.library) apply false
    alias(libs.plugins.jetbrains.kotlin.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.binary.compatibility.validator)
    alias(libs.plugins.maven.publish) apply false
}

subprojects {
    pluginManager.apply("jchucomponents.lint")
}

dokka {
    dokkaPublications.html {
        moduleName.set("JchuComponents")
        moduleVersion.set(libs.versions.jchucomponents.get())
        outputDirectory.set(layout.buildDirectory.dir("dokka/html"))
        includes.from(layout.projectDirectory.file("README.md"))
    }

    pluginsConfiguration.html {
        customStyleSheets.from(layout.projectDirectory.file("docs/styles/jchucomponents.css"))
        customAssets.from(
            layout.projectDirectory.file("jchucomponents-ui/src/main/res/drawable/ic_deco_jeluchu.webp"),
            layout.projectDirectory.file("app/src/main/res/font/light.ttf"),
            layout.projectDirectory.file("app/src/main/res/font/regular.ttf"),
            layout.projectDirectory.file("app/src/main/res/font/medium.ttf"),
            layout.projectDirectory.file("app/src/main/res/font/bold.ttf"),
            layout.projectDirectory.file("app/src/main/res/font/heavy.ttf"),
        )
        footerMessage.set("© Jeluchu. JchuComponents documentation.")
    }
}

dependencies {
    dokka(project(":jchucomponents-core"))
    dokka(project(":jchucomponents-foundation"))
    dokka(project(":jchucomponents-ktx"))
    dokka(project(":jchucomponents-network"))
    dokka(project(":jchucomponents-pay"))
    dokka(project(":jchucomponents-prefs"))
    dokka(project(":jchucomponents-qr"))
    dokka(project(":jchucomponents-room"))
    dokka(project(":jchucomponents-supabase"))
    dokka(project(":jchucomponents-ui"))
}

apiValidation {
    ignoredProjects += listOf("app", "jchucomponents-bom")
    ignoredPackages += listOf(
        "com.jeluchu.composer",
        "com.jeluchu.jchucomponents.ui.accompanist"
    )
}

val ciCheck = tasks.register("ciCheck") {
    group = "verification"
    description = "Runs the repository-wide Android and Kotlin release checks."
}

gradle.projectsEvaluated {
    val verificationTaskNames = setOf(
        "apiCheck",
        "check",
        "assembleDebug",
        "assembleRelease",
        "publishToMavenLocal",
    )
    ciCheck.configure {
        dependsOn(
            allprojects.flatMap { project ->
                project.tasks.matching { it.name in verificationTaskNames }
            },
        )
    }
}
