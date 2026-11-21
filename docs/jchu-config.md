# JchuConfig guide

`JchuConfig { }` is the single startup block every JchuComponents app uses. You declare the modules the app needs,
and the library initializes them in the right order and registers them in Koin. There is no per-app wiring in
`Application`, `KoinHelper` or `main()`.

This guide covers how to use it, the available blocks, the pattern every app follows, and how to add a block for
a new module.

## Mental model

- **Declarative.** Only the blocks you write are initialized. Nothing is auto-detected.
- **Modular.** A block lives in the module it configures. If the app does not depend on `jchucomponents-supabase`,
  there is no `supabase { }` block and the module is not pulled in.
- **Ordered.** Blocks may depend on each other; they are initialized in dependency order.
- **Fails early.** A duplicate block, a missing dependency or a blank key fails at startup with a message that
  names the block.

## Quick start

Add the dependencies you use (`jchucomponents-config` comes transitively with any module that has a block):

```kotlin
implementation(libs.jchucomponents.prefs)
implementation(libs.jchucomponents.supabase)
```

Android, in `Application.onCreate`:

```kotlin
JchuConfig(this) {
    preferences { fileName = "my_app.preferences_pb" }
    crashlytics { collectionEnabled = !BuildConfig.DEBUG }
    analytics { collectionEnabled = !BuildConfig.DEBUG }
    payments {
        apiKey = getString(R.string.revenuecat_api_key)
        isDebug = BuildConfig.DEBUG
        subscriptionName = "premium"
    }
    supabase {
        url = getString(R.string.supabase_url)
        publishableKey = getString(R.string.supabase_publishable_key)
        optional = true
    }
    koin { modules(appModule) }
}
```

iOS, macOS and desktop use the same blocks from shared Kotlin, without a platform argument. Call the function from
your Swift `App` initializer:

```kotlin
fun startApp() {
    JchuConfig {
        preferences { fileName = "my_app.preferences_pb" }
        crashlytics()
        analytics()
        koin { modules(appModule) }
    }
}
```

## Blocks

| Block | Module | Result |
| --- | --- | --- |
| `preferences { fileName }` | `jchucomponents-prefs` | `JchuPreferences` (DataStore) and `JchuPreferencesHelper` (synchronous reads and writes) in Koin |
| `crashlytics { collectionEnabled; backend }` | `jchucomponents-monitoring` | `JchuMonitoring.crash` (log, record exceptions, user id, custom keys) |
| `analytics { collectionEnabled; backend }` | `jchucomponents-monitoring` | `JchuMonitoring.analytics` (events, screens, user id and properties) |
| `payments { apiKey; isDebug; subscriptionName }` | `jchucomponents-pay-kmp` | Initializes RevenueCat |
| `supabase { url; publishableKey; auth; modules; optional }` | `jchucomponents-supabase` | Validated Supabase client and sub-clients in Koin |
| `network { baseUrl; enableLogging }` | `jchucomponents-network` | A shared Ktor `HttpClient` in Koin |
| `database<T>(name) { migrations; fallbackToDestructiveMigration }` | `jchucomponents-room` | A Room 3 database in Koin, one call per database |
| `koin { modules(...) }` | `jchucomponents-config` | Starts Koin with the feature modules plus yours |

### Preferences

`preferences { }` registers two things. `JchuPreferences` is the DataStore wrapper with `suspend` and `Flow` access.
`JchuPreferencesHelper` is a synchronous layer over the same DataStore for flags you need to read without
suspending (`getBoolean(key, default)`, `saveInt(key, value)`, `savePreference(tag, value)`...). Its cache fills in
the background, so a read in the first milliseconds after configuration returns the default value.

The app only declares its own variables. Keep them in one `PreferencesService`:

```kotlin
object PreferencesService {
    private const val KEY_FIRST_LAUNCH = "first_launch"
    private val helper get() = jchuPreferencesHelper()

    var isFirstLaunch: Boolean
        get() = helper.getBoolean(KEY_FIRST_LAUNCH, true)
        set(value) = helper.saveBoolean(KEY_FIRST_LAUNCH, value)
}
```

### Crash reporting and analytics

Both blocks are safe by design: calling `JchuMonitoring.crash` or `JchuMonitoring.analytics` before configuration, on
a platform without Firebase, or when the backend throws, does nothing instead of failing.

```kotlin
JchuMonitoring.crash.recordException(error, context = "Checkout")
JchuMonitoring.analytics.logEvent("purchase_started", mapOf("plan" to "yearly"))
JchuMonitoring.analytics.logScreen("Home")
```

Event and parameter names are sanitized to Firebase's rules (letters, digits and `_`, at most 40 characters, at most
25 parameters). The app owns the Firebase SDK and its version, because Gradle plugin setup and
`google-services.json` / `GoogleService-Info.plist` are always per app:

- Android: add `firebase-bom`, `firebase-crashlytics` and `firebase-analytics` plus the Google services and
  Crashlytics Gradle plugins. The library compiles against them but does not force a version.
- iOS: add the Firebase SDK (`FirebaseCrashlytics`, `FirebaseAnalytics`) with Swift Package Manager and call
  `FirebaseApp.configure()` before `startApp()`. No Swift bridge is needed: the library reaches Firebase through the
  Objective-C runtime and does nothing if the SDK is not linked.

To send events somewhere else, pass your own `backend = ...` to either block.

### Purchases

`payments { }` only configures RevenueCat. `JchuPurchases` (same module) is what apps call afterwards, so no app
has to wrap the SDK callbacks itself. Every call is `suspend`, has a timeout and fails with `JchuPurchasesException`
(also when `payments { optional = true }` skipped an unusable key, see `JchuPurchases.isConfigured`).

```kotlin
JchuPurchases.identify(userId)                       // log in, log out (null/blank) or nothing if already identified
val offering = JchuPurchases.offering()              // the current one; offering("id") for another
val annual = JchuPurchases.findPackage("annual")     // in the current offering or in offering("id")
when (val result = JchuPurchases.purchase(annual)) {
    is JchuPurchaseResult.Purchased -> result.customer.isActive("premium")
    JchuPurchaseResult.Cancelled -> Unit             // the user closed the store sheet: not an error
}
val customer = JchuPurchases.restore()               // or refreshCustomer()
JchuPurchases.customer                               // StateFlow<JchuCustomer?> with the last state seen
```

`JchuCustomer` exposes `isActive(entitlement)`, `activeProduct(entitlement)` and every `JchuEntitlement`, so apps with
several entitlements read them all from the same object. Map the SDK `Offering` and `Package` to your own models in
the app. `Payment` (billing info and ready-made monthly/annual paywall data) is unchanged.

### Room databases

`database<T>` is platform-specific because Room 3 has no common builder: call it from the Android `Application` and
from the Apple host in `appleMain` (iOS and macOS) or the desktop `main()` (JVM). Room 3 only; apps on `androidx.room` 2.x must migrate first.

```kotlin
// Android
database<CacheDatabase>("cache.db") { fallbackToDestructiveMigration = true }

// iOS (Kotlin/Native has no reflection, so pass the generated constructor)
database<CacheDatabase>("cache.db", factory = { CacheDatabaseConstructor.initialize() })
```

## The pattern every app follows

1. One `JchuConfig` call at startup per platform, listing only the blocks the app uses.
2. App code never initializes a library module by hand.
3. App-level preferences live in one `PreferencesService` over `jchuPreferencesHelper()`.
4. Errors and usage go through `JchuMonitoring`, never through Firebase types in shared code.
5. Supabase, database and payment keys come from the platform's resources, never from source.

## Keeping control of Koin

`koin { }` is optional. Without it nothing is started and the result exposes the modules so you can start Koin
yourself:

```kotlin
val jchu = JchuConfig(this) { preferences() }
startKoin {
    androidContext(this@MyApplication)
    modules(jchu.modules + appModule)
}
```

## Adding a block for a new module

1. Create the module with the `jchucomponents.kmp-library` and `jchucomponents.publish` convention plugins and
   `api(project(":jchucomponents-config"))`.
2. Implement `JchuFeature` and expose an extension on `JchuConfigBuilder`:

```kotlin
fun JchuConfigBuilder.myFeature(block: MyBlock.() -> Unit = {}) =
    install(MyFeature(MyBlock().apply(block)))

internal class MyFeature(private val options: MyBlock) : JchuFeature {
    override val id = "my-feature"
    override val dependsOn = setOf("preferences")        // optional
    override fun initialize(platform: JchuPlatform) { }   // optional, SDK setup
    override fun modules(platform: JchuPlatform) = listOf(module { /* ... */ })
}
```

3. Validate options in the block function with `require(...)` and a message that names the block.
4. Register the module in every place a release needs it. `scripts/check-release-readiness.sh` fails when one is
   missing, so you cannot forget:
   - `settings.gradle.kts` and the root `build.gradle.kts` (`dokka(project(...))`),
   - `jchucomponents-bom/build.gradle.kts`.

   The release workflow publishes every module that applies `jchucomponents.publish`, so it needs no list.
5. Add tests, run `./gradlew :your-module:apiDump` and commit the API baseline.
6. Document the block in this guide and in `jchucomponents-config/README.md`.

## Before you release

Run the release preflight first. It runs the same script the Release workflow runs, so a problem costs nothing
instead of a new version and a new tag:

```bash
scripts/release-preflight.sh          # locally
gh workflow run "Release preflight"   # on GitHub, on a macOS runner
```
