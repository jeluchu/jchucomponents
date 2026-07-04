plugins {
    `kotlin-dsl`
}

group = "com.jeluchu.jchucomponents.buildlogic"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    implementation("com.android.tools.build:gradle:9.2.1")
    implementation("com.vanniktech:gradle-maven-publish-plugin:0.37.0")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:14.2.0")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:2.2.0")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.4.0")
}
