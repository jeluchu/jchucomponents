/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

buildscript {

    repositories {
        maven { url = uri("https://jitpack.io") }
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.2.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.7.0")
    }

}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

tasks.register("clean", Delete::class){
    delete(rootProject.buildDir)
}