# JchuComponents Preferences

`jchucomponents-prefs` provides a Kotlin Multiplatform preferences API backed
by AndroidX DataStore Preferences. It can be used from shared Kotlin code on
Android, iOS and macOS.

## Installation

Add JitPack to the repositories used by the application:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
```

Declare the library in the version catalog:

```toml
[versions]
jchucomponents = "3.0.0-alpha07"

[libraries]
jchucomponents-prefs = {
    module = "io.github.jeluchu:jchucomponents-prefs",
    version.ref = "jchucomponents"
}
```

Add it to the shared module:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.jchucomponents.prefs)
        }
    }
}
```

The public API is available from:

```kotlin
import com.jeluchu.jchucomponents.prefs.JchuPreferences
import com.jeluchu.jchucomponents.prefs.createJchuPreferences
```

## Creating the preferences instance

Create only one `JchuPreferences` instance for each preferences file and keep
it as an application-level singleton.

### Android

Create the instance with the application context:

```kotlin
val preferences = createJchuPreferences(
    context = applicationContext
)
```

To use a custom file name:

```kotlin
val preferences = createJchuPreferences(
    context = applicationContext,
    fileName = "app.preferences_pb"
)
```

### Apple

Create the instance from an iOS or macOS Kotlin source set:

```kotlin
val preferences = createJchuPreferences()
```

To use a custom file name:

```kotlin
val preferences = createJchuPreferences(
    fileName = "app.preferences_pb"
)
```

The default file name on both platforms is
`jchucomponents.preferences_pb`.

## Shared usage

Inject `JchuPreferences` into a repository defined in `commonMain`:

```kotlin
class AppPreferences(
    private val preferences: JchuPreferences
) {
    val darkMode: Flow<Boolean> =
        preferences.observeBoolean("dark_mode")

    suspend fun setDarkMode(enabled: Boolean) {
        preferences.setBoolean("dark_mode", enabled)
    }

    suspend fun getAuthToken(): String =
        preferences.getString("auth_token")

    suspend fun setAuthToken(token: String) {
        preferences.setString("auth_token", token)
    }

    suspend fun removeAuthToken() {
        preferences.remove("auth_token")
    }
}
```

The supported convenience types are `Int`, `Long`, `Float`, `Double`,
`String`, `Boolean`, and `Set<String>`. Each type provides `observe`, `get`,
and `set` operations. The API also supports checking keys, removing one or
more keys, editing several values atomically, and clearing the complete store.

## Dependency injection

Register the platform-created `JchuPreferences` instance as a singleton in the
application dependency container:

```kotlin
single<JchuPreferences> {
    createJchuPreferences(
        context = androidContext(),
        fileName = "app.preferences_pb"
    )
}

single {
    AppPreferences(get())
}
```

On iOS and macOS, create the same application dependency graph with the Apple
factory:

```kotlin
val preferences = createJchuPreferences(
    fileName = "app.preferences_pb"
)
val appPreferences = AppPreferences(preferences)
```

Do not create multiple DataStore instances for the same file. Keep the
platform-specific creation at the application boundary and use the injected
repository from shared business logic.
