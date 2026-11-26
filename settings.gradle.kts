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
        repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
        repositories {
                google()
                mavenCentral()
                gradlePluginPortal()
                maven("https://jitpack.io")
                // Node and Yarn downloads needed by the Kotlin/Wasm browser toolchain (samples/web).
                exclusiveContent {
                        forRepository {
                                ivy("https://nodejs.org/dist") {
                                        name = "Node.js distributions"
                                        patternLayout { artifact("v[revision]/[artifact](-v[revision]-[classifier]).[ext]") }
                                        metadataSources { artifact() }
                                }
                        }
                        filter { includeModule("org.nodejs", "node") }
                }
                exclusiveContent {
                        forRepository {
                                ivy("https://github.com/yarnpkg/yarn/releases/download") {
                                        name = "Yarn distributions"
                                        patternLayout { artifact("v[revision]/[artifact](-v[revision]).[ext]") }
                                        metadataSources { artifact() }
                                }
                        }
                        filter { includeModule("com.yarnpkg", "yarn") }
                }
                exclusiveContent {
                        forRepository {
                                ivy("https://github.com/WebAssembly/binaryen/releases/download") {
                                        name = "Binaryen distributions"
                                        patternLayout { artifact("version_[revision]/binaryen-version_[revision]-[classifier].[ext]") }
                                        metadataSources { artifact() }
                                }
                        }
                        filter { includeModule("com.github.webassembly", "binaryen") }
                }
        }
}

rootProject.name = "JchuComponents"
include(
        ":jchucomponents-bom",
        ":jchucomponents-ui",
        ":jchucomponents-pdf",
        ":jchucomponents-navigation3",
        ":jchucomponents-android",
        ":jchucomponents-qr",
        ":jchucomponents-pay",
        ":jchucomponents-prefs",
        ":jchucomponents-room",
        ":jchucomponents-foundation",
        ":jchucomponents-config",
        ":jchucomponents-monitoring",
        ":jchucomponents-network",
        ":jchucomponents-supabase",
        "app",
        ":samples:web"
)
