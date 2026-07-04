plugins {
    id("com.vanniktech.maven.publish")
}

group = "io.github.jeluchu"

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)

    if (providers.gradleProperty("signingInMemoryKey").isPresent) {
        signAllPublications()
    }

    pom {
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
            developerConnection.set(
                "scm:git:ssh://git@github.com/Jeluchu/jchucomponents.git"
            )
        }
    }
}
