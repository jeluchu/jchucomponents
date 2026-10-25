pluginManagement {
        includeBuild("build-logic")
        repositories {
                google()
                mavenCentral()
                gradlePluginPortal()
                maven("https://jitpack.io")
        }
}
dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories {
                google()
                mavenCentral()
                gradlePluginPortal()
                maven("https://jitpack.io")
        }
}

rootProject.name = "JchuComponents"
include(
        ":jchucomponents-bom",
        ":jchucomponents-core",
        ":jchucomponents-ui",
        ":jchucomponents-pdf",
        ":jchucomponents-navigation3",
        ":jchucomponents-navigation3-di",
        ":jchucomponents-app-updates",
        ":jchucomponents-ktx",
        ":jchucomponents-qr",
        ":jchucomponents-pay",
        ":jchucomponents-prefs",
        ":jchucomponents-room",
        ":jchucomponents-foundation",
        ":jchucomponents-network",
        ":jchucomponents-supabase",
        "app"
)
