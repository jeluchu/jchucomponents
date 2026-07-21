![JchuComponents cover](https://raw.githubusercontent.com/Jeluchu/jchucomponents-compose/develop/images/cover.png)

# JchuComponents

JchuComponents is a collection of Kotlin Multiplatform utilities, Android
Jetpack Compose components and native SwiftUI components for building Android
and Apple applications with less repeated infrastructure.

> JchuComponents 3 is under active development on the `v3` branch. Pre-release
> versions follow `3.0.0-alphaNN`, `3.0.0-betaNN` and `3.0.0-rcNN` before the
> stable `3.0.0` release.

## Modules and platforms

| Module | Android | iOS | macOS | Purpose |
| --- | :---: | :---: | :---: | --- |
| `jchucomponents-foundation` | ✅ | ✅ | ✅ | Portable state, dates, text and utilities |
| `jchucomponents-network` | ✅ | ✅ | ✅ | Ktor client configuration, resources and flow helpers |
| `jchucomponents-prefs` | ✅ | ✅ | ✅ | Multiplatform DataStore preferences |
| `jchucomponents-pay` | ✅ | ✅ | ✅ | Shared payment models and platform integrations |
| `jchucomponents-qr` | ✅ | ✅ | ✅ | QR encoding and decoding utilities |
| `jchucomponents-room` | ✅ | ✅ | — | Room 3 runtime configuration and SQL migrations |
| `jchucomponents-supabase` | ✅ | ✅ | ✅ | Supabase Auth, database, realtime, Edge Functions and Koin setup |
| `jchucomponents-core` | ✅ | — | — | Android architecture and lifecycle utilities |
| `jchucomponents-ktx` | ✅ | — | — | Android and Kotlin extensions |
| `jchucomponents-ui` | ✅ | — | — | Jetpack Compose components |
| `jchucomponents-bom` | ✅ | — | — | Aligned Android module versions |

The Swift package complements the shared Kotlin code with
`JchuComponentsCore`, `JchuComponentsExtensions`, `JchuComponentsSwiftUI` and
the optional native `JchuComponentsPay` product.

## Installation

The examples below use `3.0.0-alpha08`. Replace it with the
[latest available release](https://github.com/Jeluchu/jchucomponents/releases).

### Kotlin Multiplatform

KMP artifacts are published to Maven Central. Add only the modules needed by
your shared source set:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(
                "io.github.jeluchu:jchucomponents-foundation:3.0.0-alpha08"
            )
            implementation(
                "io.github.jeluchu:jchucomponents-network:3.0.0-alpha08"
            )
        }
    }
}
```

The available Maven Central artifacts are:

```text
io.github.jeluchu:jchucomponents-foundation
io.github.jeluchu:jchucomponents-network
io.github.jeluchu:jchucomponents-pay
io.github.jeluchu:jchucomponents-prefs
io.github.jeluchu:jchucomponents-qr
io.github.jeluchu:jchucomponents-room
io.github.jeluchu:jchucomponents-supabase
io.github.jeluchu:jchucomponents-bom
```

These artifacts contain Android, iOS and macOS variants except
`jchucomponents-room`, which currently contains Android, `iosArm64`, and
`iosSimulatorArm64`. JitPack's Linux builds do not produce the Apple KLIB
variants, so KMP applications targeting iOS or macOS should use Maven Central.

### Android

Add JitPack to `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
```

Use the BOM to keep Android modules on the same version:

```kotlin
dependencies {
    implementation(
        platform("io.github.jeluchu:jchucomponents-bom:3.0.0-alpha08")
    )
    implementation("com.github.jeluchu.jchucomponents:jchucomponents-ui")
    implementation("com.github.jeluchu.jchucomponents:jchucomponents-ktx")
}
```

Alternatively, specify each JitPack artifact version directly:

```kotlin
dependencies {
    implementation(
        "com.github.jeluchu.jchucomponents:jchucomponents-ui:3.0.0-alpha08"
    )
}
```

### Swift Package Manager

Add the following package URL in Xcode:

```text
https://github.com/Jeluchu/jchucomponents-spm
```

Select the `JchuComponents` product and import the modules needed by the
application:

```swift
import JchuComponentsCore
import JchuComponentsExtensions
import JchuComponentsSwiftUI
```

For the native RevenueCat integration, also select and import the optional
product:

```swift
import JchuComponentsPay
```

The Swift package currently requires iOS 26.0 or newer. Releases live in the
dedicated `jchucomponents-spm` repository so consumers do not clone the full
Kotlin and Android monorepo.

## Network quick start

Create a platform-specific Ktor engine from shared Kotlin:

```kotlin
val client = createHttpClient(
    HttpClientConfiguration(
        baseUrl = "https://example.com/api/",
        enableLogging = isDebug,
        defaultHeaders = mapOf("X-Client" to "my-app"),
    )
)
```

`createHttpClient` uses Ktor's Android engine on Android and Darwin engine on
iOS and macOS. `HttpClientConfiguration` controls JSON, caching, timeouts,
response validation, logging and sensitive headers.

The v3 network result models and flow helpers belong to
`jchucomponents-network`:

```kotlin
import com.jeluchu.jchucomponents.network.extensions.flow.flowCollector
import com.jeluchu.jchucomponents.network.extensions.flow.flowResourceCollector
import com.jeluchu.jchucomponents.network.extensions.handleFailure
import com.jeluchu.jchucomponents.network.models.Failure
import com.jeluchu.jchucomponents.network.models.Resource
```

Their previous `jchucomponents-core` packages were removed during the v3 alpha
cycle.

## Room quick start

Apply the shared runtime defaults to a Room 3 builder while retaining access to
all application-specific configuration:

```kotlin
val database = Room.databaseBuilder<AppDatabase>(context, "app.db")
    .configureJchuRoom {
        addMigrations(
            sqlMigration(
                startVersion = 1,
                endVersion = 2,
                "ALTER TABLE task ADD COLUMN archived INTEGER NOT NULL DEFAULT 0"
            )
        )
    }
    .build()
```

The consuming module must apply KSP and add the
`androidx.room3:room3-compiler` dependency itself. The runtime module does not
enable destructive migrations or choose a database path.

## Supabase quick start

Register the shared Supabase client from the same Koin graph used by the app
repositories:

```kotlin
val supabaseModule = jchuSupabaseModule(
    JchuSupabaseConfig(
        url = BuildKonfig.SUPABASE_URL,
        publishableKey = BuildKonfig.SUPABASE_PUBLISHABLE_KEY
    )
)
```

Repositories can depend on `JchuSupabaseDatabase`, `JchuSupabaseAuth`,
`JchuSupabaseFunctions` or `JchuSupabaseRealtime` and keep returning
`Flow<Resource<Failure, T>>`:

```kotlin
@Serializable
data class Profile(
    val id: String,
    val name: String
)

object ProfileTable : JchuSupabaseTable<Profile> {
    override val name: String = "profiles"
}

class ProfileRepository(
    database: JchuSupabaseDatabase
) {
    private val profiles = database.table(ProfileTable)

    fun profile(id: String): Flow<Resource<Failure, Profile>> =
        profiles.findById(id)

    fun updateProfile(profile: Profile): Flow<Resource<Failure, Profile>> =
        profiles.updateById(
            id = profile.id,
            value = profile
        )
}
```

The first Supabase module intentionally excludes Storage. It focuses on Auth,
PostgREST database access, Realtime wiring, Edge Functions, Koin and resource
flow helpers for Android, iOS and macOS KMP apps.

## API stability

- Alpha releases may introduce source and binary-breaking changes.
- Beta releases stabilize the public API; breaking changes should be rare and
  documented.
- Release candidates accept fixes and documentation changes unless a critical
  API issue is found.
- Stable releases follow semantic versioning.
- Public Kotlin API changes are checked by the binary compatibility validator.
- Removed or renamed APIs are recorded in [CHANGELOG.md](CHANGELOG.md).

Experimental APIs should be explicitly marked with `@RequiresOptIn`. Once an
API becomes stable, incompatible changes require a deprecation cycle whenever
practical.

## Development

The repository includes an Android catalog in `app` and an iOS catalog in
`samples/iosApp`.

Run the repository-wide Android and Kotlin checks:

```bash
./gradlew ciCheck
```

This validates binary APIs, tests, debug and release assemblies, and all local
Maven publications. See the [consumer guide](docs/getting-started.md) for
focused setup and examples for every module.

Inspect Kotlin formatting without modifying source files:

```bash
./gradlew ktlintCheck
```

HTML and plain-text reports are generated under each module's
`build/reports/ktlint` directory. The check is currently advisory and does not
fail CI. Apply safe automatic fixes to one module at a time:

```bash
./gradlew :jchucomponents-foundation:ktlintFormat
```

Review the resulting diff before continuing with another module because some
rules, such as file naming, require a manual decision.

Build the Android catalog:

```bash
./gradlew :app:assembleDebug
```

Verify the artifacts as an external KMP consumer after publishing them locally:

```bash
./gradlew \
  :jchucomponents-foundation:publishToMavenLocal \
  :jchucomponents-network:publishToMavenLocal \
  :jchucomponents-pay:publishToMavenLocal \
  :jchucomponents-prefs:publishToMavenLocal \
  :jchucomponents-qr:publishToMavenLocal \
  :jchucomponents-room:publishToMavenLocal \
  :jchucomponents-supabase:publishToMavenLocal

./gradlew \
  -p smoke-tests/kmp-consumer \
  compileAndroidMain \
  compileKotlinIosSimulatorArm64 \
  compileKotlinMacosArm64
```

The smoke project reserves `io.github.jeluchu` for Maven Local, ensuring the
check cannot silently consume an already published release from Maven Central.

Build the Swift package after assembling the XCFramework:

```bash
./gradlew \
  :jchucomponents-foundation:assembleJchuComponentsCoreReleaseXCFramework

xcodebuild \
  -scheme JchuComponents-Package \
  -destination 'generic/platform=iOS Simulator' \
  build
```

For contributions:

1. Open a bug report or feature request.
2. Discuss the proposed public API for larger changes.
3. Add implementation, tests and documentation.
4. Open a [pull request](https://github.com/Jeluchu/jchucomponents/pulls).

## Synchronizing localizations

[`scripts/sync_strings.py`](scripts/sync_strings.py) synchronizes Android
`strings.xml` resources into an Apple `Localizable.xcstrings` catalog. It adds
new keys and locales, updates translations, converts placeholders such as
`%1$s` to `%1$@` and ignores non-translatable strings.

Register it once as a Gradle task in the Android application module:

```kotlin
tasks.register<Exec>("syncIosStrings") {
    group = "localization"
    description = "Syncs Android strings into the iOS String Catalog"
    commandLine(
        "python3",
        rootProject.file("scripts/sync_strings.py"),
        "--xcstrings",
        rootProject.file(
            "iosApp/iosApp/resources/strings/Localizable.xcstrings"
        ),
        "--android-res",
        project.file("src/main/res"),
        "--delete-missing",
    )
}
```

From then on, synchronization only requires running the Gradle task:

```bash
./gradlew :androidApp:syncIosStrings
```

It can also be saved as an Android Studio **Gradle Run Configuration**:

- Name: `Sync localizables`
- Run: `:androidApp:syncIosStrings`
- Gradle project: the root KMP project

This exposes synchronization as a one-click action from the IDE. Enable
**Store as project file** if the run configuration should be shared with the
rest of the team.

Remove `--delete-missing` when Apple-only keys must be preserved. Add
`"--dry-run"` to `commandLine` when the task should only preview changes.

## Release process

Tagged `3.*` versions trigger the release workflow. It validates the Gradle
version, API compatibility, tests, Android publications and the Swift package;
then it publishes the signed KMP variants and BOM to Maven Central and prepares
the dedicated SwiftPM repository.

Before tagging, update `jchucomponents` in `gradle/libs.versions.toml` and make
sure CI passes on `v3`:

```bash
git tag -a 3.0.0-alpha08 -m "3.0.0-alpha08"
git push origin v3
git push origin 3.0.0-alpha08
```

SwiftPM publication additionally requires the repository variable
`SPM_REPOSITORY_ENABLED=true` and a `SPM_REPOSITORY_TOKEN` secret with write
access to `Jeluchu/jchucomponents-spm`.

## License

JchuComponents is available under the [Apache License 2.0](LICENSE).
