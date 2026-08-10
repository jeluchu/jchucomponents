plugins {
    id("jchucomponents.kmp-library")
    id("jchucomponents.publish")
    alias(libs.plugins.kotlinx.serialization)
}

version = libs.versions.jchucomponents.get()
description = "Kotlin Multiplatform Supabase integration for Auth, database, realtime and Edge Functions."

kotlin {
    android {
        namespace = "com.jeluchu.jchucomponents.supabase"
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":jchucomponents-network"))
            api(project.dependencies.platform(libs.supabase.bom))
            api(libs.koin.core)
            api(libs.supabase.auth)
            api(libs.supabase.functions)
            api(libs.supabase.postgrest)
            api(libs.supabase.realtime)
            api(libs.org.jetbrains.kotlinx.kotlinx.coroutines.core)
            api(libs.org.jetbrains.kotlinx.kotlinx.serialization.json)
        }

        androidMain.dependencies {
            implementation(libs.ktor.android)
        }

        appleMain.dependencies {
            implementation(libs.ktor.darwin)
        }
    }
}

mavenPublishing {
    pom {
        name.set("JchuComponents Supabase")
        description.set("Kotlin Multiplatform Supabase integration for Auth, database, realtime and Edge Functions.")
    }
}
