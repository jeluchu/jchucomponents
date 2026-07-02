import org.gradle.api.artifacts.component.ProjectComponentIdentifier
import org.gradle.api.attributes.Bundling
import org.gradle.api.attributes.Category
import org.gradle.api.attributes.LibraryElements
import org.gradle.api.attributes.Usage
import org.gradle.api.attributes.java.TargetJvmEnvironment

plugins {
    alias(libs.plugins.jetbrains.dokka) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.kmp.library) apply false
    alias(libs.plugins.jetbrains.kotlin.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.binary.compatibility.validator)
}

val metalava = configurations.create("metalava") {
    isCanBeConsumed = false
    isCanBeResolved = true
    attributes {
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
        attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.JAR))
        attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        attribute(TargetJvmEnvironment.TARGET_JVM_ENVIRONMENT_ATTRIBUTE, objects.named(TargetJvmEnvironment.STANDARD_JVM))
    }
}

dependencies {
    metalava(libs.metalava)
}

apiValidation {
    ignoredProjects += listOf("app")
    ignoredPackages += listOf(
        "com.jeluchu.composer",
        "com.jeluchu.jchucomponents.ui.accompanist"
    )
}

val androidApiProjects = listOf(
    "jchucomponents-core",
    "jchucomponents-ktx",
    "jchucomponents-pay",
    "jchucomponents-prefs",
    "jchucomponents-qr",
    "jchucomponents-ui",
)

fun Project.androidJar(): File {
    val environmentAndroidHome = providers
        .environmentVariable("ANDROID_HOME")
        .orElse(providers.environmentVariable("ANDROID_SDK_ROOT"))
        .orNull

    val localAndroidHome = file("local.properties")
        .takeIf { it.exists() }
        ?.readLines()
        ?.firstOrNull { it.startsWith("sdk.dir=") }
        ?.substringAfter("sdk.dir=")

    val androidHome = environmentAndroidHome
        ?: localAndroidHome
        ?: error("ANDROID_HOME, ANDROID_SDK_ROOT or local.properties sdk.dir must be set to run Android API checks.")

    val platformsDirectory = file("$androidHome/platforms")
    val preferredAndroidJar = listOf("android-37", "android-37.0")
        .map { platformsDirectory.resolve("$it/android.jar") }
        .firstOrNull { it.exists() }

    return preferredAndroidJar
        ?: platformsDirectory
            .listFiles { file -> file.isDirectory && file.name.startsWith("android-") }
            .orEmpty()
            .sortedBy { it.name }
            .map { it.resolve("android.jar") }
            .lastOrNull { it.exists() }
        ?: error("No Android platform android.jar found under ${platformsDirectory.path}.")
}

fun Project.androidApiSignatureFile(moduleName: String) =
    layout.projectDirectory.file("api/android/$moduleName.txt")

fun Project.androidApiGeneratedFile(moduleName: String) =
    layout.buildDirectory.file("api/android/$moduleName.txt")

fun Project.androidExternalClasspath(moduleProject: Project): FileCollection {
    val releaseCompileClasspath = moduleProject.configurations.getByName("releaseCompileClasspath")
    return releaseCompileClasspath.incoming.artifactView {
        componentFilter { componentId -> componentId !is ProjectComponentIdentifier }
    }.files
}

fun Project.androidProjectClasspath(moduleName: String): FileCollection {
    val dependencyModules = when (moduleName) {
        "jchucomponents-ui" -> listOf("jchucomponents-foundation", "jchucomponents-ktx")
        else -> emptyList()
    }

    return files(
        dependencyModules.flatMap { dependencyModule ->
            val dependencyProject = project(":$dependencyModule")
            listOf(
                dependencyProject
                    .layout
                    .buildDirectory
                    .file("intermediates/compile_library_classes_jar/release/bundleLibCompileToJarRelease/classes.jar"),
                dependencyProject
                    .layout
                    .buildDirectory
                    .file("intermediates/compile_library_classes_jar/androidMain/bundleAndroidMainClassesToCompileJar/classes.jar"),
            )
        }
    )
}

androidApiProjects.forEach { moduleName ->
    val moduleProject = project(":$moduleName")
    val capitalizedName = moduleName
        .split("-")
        .joinToString("") { it.replaceFirstChar(Char::uppercaseChar) }

    val dumpTask = tasks.register<JavaExec>("dump${capitalizedName}Api") {
        group = "verification"
        description = "Generates the Metalava API signature for :$moduleName."

        val outputFile = androidApiGeneratedFile(moduleName)
        outputs.file(outputFile)

        dependsOn(":$moduleName:assembleRelease")
        classpath = metalava
        mainClass.set("com.android.tools.metalava.Driver")

        doFirst {
            outputFile.get().asFile.parentFile.mkdirs()
        }

        doFirst {
            val extractedClasspathDirectory = layout
                .buildDirectory
                .dir("api/android/$moduleName-classpath")
                .get()
                .asFile

            delete(extractedClasspathDirectory)
            extractedClasspathDirectory.mkdirs()

            val rawClasspath = files(
                androidJar(),
                androidExternalClasspath(moduleProject),
                androidProjectClasspath(moduleName),
            ).files

            val metalavaClasspath = rawClasspath.mapNotNull { classpathFile ->
                when {
                    classpathFile.extension == "aar" -> {
                        val extractedJar = extractedClasspathDirectory
                            .resolve("${classpathFile.nameWithoutExtension}-classes.jar")

                        copy {
                            from(zipTree(classpathFile))
                            include("classes.jar")
                            into(extractedClasspathDirectory)
                            rename { extractedJar.name }
                        }

                        extractedJar.takeIf { it.exists() }
                    }
                    classpathFile.exists() -> classpathFile
                    else -> null
                }
            }

            args(
                "--source-path",
                moduleProject.file("src/main/kotlin").absolutePath,
                "--classpath",
                files(metalavaClasspath).asPath,
                "--api",
                outputFile.get().asFile.absolutePath,
                "--format=v2",
                "--quiet",
            )
        }

        doLast {
            val generatedApi = outputFile.get().asFile
            generatedApi.writeText(generatedApi.readText().trimEnd() + "\n")
        }
    }

    tasks.register<Copy>("update${capitalizedName}Api") {
        group = "verification"
        description = "Updates the committed Metalava API signature for :$moduleName."

        dependsOn(dumpTask)
        from(androidApiGeneratedFile(moduleName))
        into(layout.projectDirectory.dir("api/android"))
        rename { "$moduleName.txt" }
    }

    tasks.register("check${capitalizedName}Api") {
        group = "verification"
        description = "Checks the Metalava API signature for :$moduleName."

        dependsOn(dumpTask)

        doLast {
            val expected = androidApiSignatureFile(moduleName).asFile
            val actual = androidApiGeneratedFile(moduleName).get().asFile

            if (!expected.exists()) {
                error(
                    "Missing API signature: ${expected.path}. " +
                        "Run ./gradlew update${capitalizedName}Api after reviewing the generated API."
                )
            }

            if (expected.readText() != actual.readText()) {
                error(
                    "API signature changed for :$moduleName. " +
                        "Review ${actual.path} and run ./gradlew update${capitalizedName}Api if intentional."
                )
            }
        }
    }
}

tasks.register("androidApiDump") {
    group = "verification"
    description = "Generates Metalava API signatures for Android AAR modules."
    dependsOn(androidApiProjects.map { moduleName ->
        val capitalizedName = moduleName
            .split("-")
            .joinToString("") { it.replaceFirstChar(Char::uppercaseChar) }
        "dump${capitalizedName}Api"
    })
}

tasks.register("androidApiUpdate") {
    group = "verification"
    description = "Updates committed Metalava API signatures for Android AAR modules."
    dependsOn(androidApiProjects.map { moduleName ->
        val capitalizedName = moduleName
            .split("-")
            .joinToString("") { it.replaceFirstChar(Char::uppercaseChar) }
        "update${capitalizedName}Api"
    })
}

tasks.register("androidApiCheck") {
    group = "verification"
    description = "Checks Metalava API signatures for Android AAR modules."
    dependsOn(androidApiProjects.map { moduleName ->
        val capitalizedName = moduleName
            .split("-")
            .joinToString("") { it.replaceFirstChar(Char::uppercaseChar) }
        "check${capitalizedName}Api"
    })
}
