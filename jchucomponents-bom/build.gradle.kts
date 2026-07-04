plugins {
    `java-platform`
    alias(libs.plugins.maven.publish)
}

group = "io.github.jeluchu"
version = libs.versions.jchucomponents.get()

javaPlatform {
    allowDependencies()
}

val jchuComponentsVersion = libs.versions.jchucomponents.get()

dependencies {
    constraints {
        api("io.github.jeluchu:jchucomponents-foundation:$jchuComponentsVersion")
        api("io.github.jeluchu:jchucomponents-network:$jchuComponentsVersion")
        api("io.github.jeluchu:jchucomponents-pay:$jchuComponentsVersion")
        api("io.github.jeluchu:jchucomponents-prefs:$jchuComponentsVersion")
        api("io.github.jeluchu:jchucomponents-qr:$jchuComponentsVersion")

        api("com.github.jeluchu:jchucomponents-core:$jchuComponentsVersion")
        api("com.github.jeluchu:jchucomponents-ktx:$jchuComponentsVersion")
        api("com.github.jeluchu:jchucomponents-ui:$jchuComponentsVersion")

        api("com.github.jeluchu.jchucomponents:jchucomponents-core:$jchuComponentsVersion")
        api("com.github.jeluchu.jchucomponents:jchucomponents-ktx:$jchuComponentsVersion")
        api("com.github.jeluchu.jchucomponents:jchucomponents-ui:$jchuComponentsVersion")
    }
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    if (providers.gradleProperty("signingInMemoryKey").isPresent) {
        signAllPublications()
    }

    coordinates(
        groupId = "io.github.jeluchu",
        artifactId = "jchucomponents-bom",
        version = libs.versions.jchucomponents.get(),
    )

    pom {
        name.set("JchuComponents BOM")
        description.set("Bill of materials for aligning JchuComponents artifact versions.")
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
