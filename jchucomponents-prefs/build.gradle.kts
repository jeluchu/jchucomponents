plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.maven.publish)
}

group = "io.github.jeluchu"
version = libs.versions.jchucomponents.get()

kotlin {
    android {
        namespace = "com.jeluchu.jchucomponents.prefs"
        compileSdk = 37
        minSdk = 21

        withSourcesJar(publish = true)
        withHostTest {}
    }

    iosArm64()
    iosX64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            api(libs.androidx.datastore)
            implementation(libs.org.jetbrains.kotlinx.kotlinx.coroutines.core)
        }

        commonTest.dependencies {
            implementation(kotlin(simpleModuleName = "test"))
        }
    }
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    if (providers.gradleProperty("signingInMemoryKey").isPresent) {
        signAllPublications()
    }

    coordinates(
        groupId = "io.github.jeluchu",
        artifactId = "jchucomponents-prefs",
        version = libs.versions.jchucomponents.get(),
    )

    pom {
        name.set("JchuComponents Preferences")
        description.set("Kotlin Multiplatform DataStore preferences for Android and iOS.")
        inceptionYear.set("2022")
        url.set("https://github.com/Jeluchu/jchucomponents")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("repo")
            }
        }

        developers {
            developer {
                id.set("jeluchu")
                name.set("Jeluchu")
                url.set("https://github.com/Jeluchu")
            }
        }

        scm {
            url.set("https://github.com/Jeluchu/jchucomponents")
            connection.set("scm:git:git://github.com/Jeluchu/jchucomponents.git")
            developerConnection.set("scm:git:ssh://git@github.com/Jeluchu/jchucomponents.git")
        }
    }
}
