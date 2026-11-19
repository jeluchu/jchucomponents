# JchuComponents Config

Full guide: [docs/jchu-config.md](../docs/jchu-config.md).

`jchucomponents-config` gives every app a single startup block, `JchuConfig { }`,
to configure the JchuComponents modules it uses (and, optionally, Koin) instead
of initializing each one by hand in `Application`, `KoinHelper` or `main()`.

Platforms: Android, iOS, macOS and JVM desktop.

## How it works

- Nothing is auto-detected: **only the blocks you write are initialized**.
- Every block lives in the module it configures (`preferences {}` in
  `jchucomponents-prefs`, `supabase {}` in `jchucomponents-supabase`, ...). If the
  app does not depend on that module the block does not exist, so no unused
  libraries are pulled in.
- Features are initialized in dependency order and contribute Koin modules.

| Block | Module | What it does |
| --- | --- | --- |
| `preferences { fileName }` | `jchucomponents-prefs` | Registers `JchuPreferences` (DataStore) and the synchronous `JchuPreferencesHelper` in Koin |
| `crashlytics { collectionEnabled; backend }` | `jchucomponents-monitoring` | Crash and non-fatal error reporting through `JchuMonitoring.crash` (Firebase Crashlytics by default) |
| `analytics { collectionEnabled; backend }` | `jchucomponents-monitoring` | Usage statistics through `JchuMonitoring.analytics` (Firebase Analytics by default) |
| `supabase { url; publishableKey; auth; modules; optional }` | `jchucomponents-supabase` | Validates the keys, creates the client at start and registers it and its sub-clients in Koin. `optional = true` skips it when the config is missing or unsafe (`isJchuSupabaseConfigured()`) |
| `payments { apiKey; isDebug; subscriptionName }` | `jchucomponents-pay-kmp` | Calls `initPayment` (RevenueCat) |
| `network { baseUrl; enableLogging; ... }` | `jchucomponents-network` | Registers a shared Ktor `HttpClient` in Koin |
| `database<T>(name) { migrations(...); fallbackToDestructiveMigration }` | `jchucomponents-room` | Registers a Room 3 database in Koin (one call per database) |
| `koin { modules(...) }` | this module | Starts Koin with the feature modules plus yours |

## Room databases

`database<T>` is platform-specific (Room 3 has no common builder), so call it from
the Android `Application` and from the iOS host in `iosMain`. Room 3 only: apps still
on `androidx.room` 2.x cannot use it until they migrate.

```kotlin
// Android
database<CacheDatabase>("AruppiCache-db") { fallbackToDestructiveMigration = true }

// iOS (Kotlin/Native has no reflection, so pass the generated constructor)
database<CacheDatabase>("AruppiCache-db", factory = { CacheDatabaseConstructor.initialize() })
```

## Android

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        JchuConfig(this) {
            preferences { fileName = "my_app.preferences_pb" }
            supabase {
                url = getString(R.string.supabase_url)
                publishableKey = getString(R.string.supabase_publishable_key)
            }
            payments {
                apiKey = getString(R.string.revenuecat_android_api_key)
                isDebug = BuildConfig.DEBUG
                subscriptionName = "premium"
            }
            network { enableLogging = BuildConfig.DEBUG }

            koin {
                if (BuildConfig.DEBUG) androidLogger()
                modules(appModule)
            }
        }
    }
}
```

The application context is registered in Koin automatically.

## iOS, macOS and desktop

No platform data is needed. Call it from shared Kotlin code (for example the
function your Swift `App` invokes at startup):

```kotlin
fun startApp() {
    JchuConfig {
        preferences()
        supabase { url = SUPABASE_URL; publishableKey = SUPABASE_KEY }
        koin { modules(appModule) }
    }
}
```

## Keeping control of Koin

`koin { }` is optional. Without it nothing is started and the result exposes the
modules so you can start Koin yourself:

```kotlin
val jchu = JchuConfig(this) { preferences() }
startKoin {
    androidContext(this@MyApplication)
    modules(jchu.modules + appModule)
}
```

## Writing a block for another module

Implement `JchuFeature` and expose an extension on `JchuConfigBuilder`:

```kotlin
fun JchuConfigBuilder.myFeature(block: MyBlock.() -> Unit) = install(MyFeature(MyBlock().apply(block)))

internal class MyFeature(...) : JchuFeature {
    override val id = "my-feature"
    override val dependsOn = setOf("preferences")      // optional
    override fun initialize(platform: JchuPlatform) { } // optional
    override fun modules(platform: JchuPlatform) = listOf(module { /* ... */ })
}
```

Misconfiguration (duplicate block, missing dependency, blank key) fails at
startup with a message naming the block.
