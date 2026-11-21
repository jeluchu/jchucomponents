# Changelog

## Unreleased

### Added

- New `jchucomponents-config` module with `JchuConfig { }`, a single startup block that
  initializes only the declared modules and can start Koin. Blocks `preferences {}`,
  `supabase {}`, `payments {}`, `network {}` and `database<T> {}` (Room 3) live in their own modules, which now
  depend on `jchucomponents-config`.
- `JchuPreferencesHelper`: synchronous preferences (`getBoolean(key, default)`, `saveInt(key, value)`, `savePreference(tag, value)`...)
  over the DataStore of `jchucomponents-prefs`, registered by `preferences {}` and read with `jchuPreferencesHelper()`, so
  apps only declare their own variables.
- New `jchucomponents-monitoring` module: `crashlytics {}` and `analytics {}` blocks and `JchuMonitoring.crash` /
  `JchuMonitoring.analytics`. Firebase is compile-only on Android and reached through the Objective-C runtime on
  Apple targets, so apps keep owning the SDK and no Swift bridge is needed.
- Release safety: `scripts/check-release-readiness.sh`, `scripts/release-preflight.sh` and the "Release preflight"
  workflow run the Release workflow's checks before a tag is pushed.
- The Release workflow now publishes the KMP artifacts (including `jchucomponents-config` and
  `jchucomponents-monitoring`) to https://github.com/jeluchu/jchucomponents-maven with
  `scripts/publish-maven-repository.sh` instead of Maven Central. Consumers add
  `maven("https://raw.githubusercontent.com/jeluchu/jchucomponents-maven/main")`; coordinates are unchanged.
  JitPack cannot serve the KMP modules because it rewrites their Gradle metadata.
- `JchuAppInfo` in `jchucomponents-config` (debug flag, version, platform and app metadata, replacing each app's
  `SystemProvider`). `payments { }` now reads `apiKey` from the `revenuecat` app metadata and `isDebug` from it
  when they are not set.
- `NetworkHandler.isWifiConnected()` on every target.
- `network(name) { }` registers extra named `HttpClient`s, and the block gains `header(name) { value }` (evaluated per
  request), `followRedirects`, `useJson`, `engine` (`JchuHttpEngine.Cio` on Android) and `allowCellularAccess`
  (Apple). The Android target now also depends on `ktor-client-cio`.
- `JchuConfigOnce { }` (idempotent start for hosts that can call it more than once) and `JchuConfigResult.koin`.
- Preference properties in `jchucomponents-prefs`: `booleanPreference`, `intPreference`, `longPreference`,
  `floatPreference`, `doublePreference`, `stringPreference` and `enumPreference`, to declare a stored variable in one line.
- `preferences { migrateFromLegacy(name, keys, removeAfter) }`: one-time copy from `SharedPreferences` or `UserDefaults`
  into the DataStore, without overwriting existing values.
- `String.removeDiacritics()` in `jchucomponents-foundation` and `ImageBitmap.toPngBytesOrNull()` in
  `jchucomponents-ui-kmp`.

- `JchuPurchases` in `jchucomponents-pay-kmp`: suspending, timeout-guarded RevenueCat operations (offerings and
  packages by id and as `JchuPackage` data, purchase by id or package with a cancelled result and an optional
  product to replace, restore, `identify`/`logOut`, `refreshCustomer`) and a
  `JchuCustomer` model with `isActive(entitlement)`, so apps stop calling the SDK callbacks directly.
- `jchucomponents-room` now also targets macOS (`macosArm64`), so `database<T> { }` works on macOS apps.
- JVM (desktop) target for `jchucomponents-foundation`, `jchucomponents-network`
  (Ktor CIO engine), `jchucomponents-prefs` (file under `~/.jchucomponents` by
  default), `jchucomponents-room` and `jchucomponents-ui-kmp`, so Compose Desktop
  apps on macOS, Windows and Linux can use them from `commonMain`. The Android and
  Apple targets are unchanged.

### Fixed

- `NetworkHandler` on iOS and macOS always reported being online; it now uses the Network framework.

## 3.0.0-alpha15

### Added

- Automatic SwiftUI navigation route generation in Xcode projects through the
  SwiftPM build-tool plugin.

### Changed

- Include the navigation graph plugin in generated SwiftPM release payloads and
  verify it before and after artifact transfer.
- Update the plugin to use the current SwiftPM plugin URL APIs.

### Fixed

- Declare the generated `Route` suffix in the `@Screen` macro so Swift can
  validate the generated route name correctly.

## 3.0.0-alpha11

### Breaking changes

- `signInWithEmail`, `signUpWithEmail` and `signOut` now return typed Auth
  results/failures instead of `Failure` with `Unit` payloads. Consumers must
  update collectors and callbacks to use `JchuSupabaseAuthFailure`,
  `JchuSupabaseAuthUser` and `JchuSupabaseSignUpResult` as applicable.

### Added

- Typed Supabase Auth users, session states and failures for login, registration
  and startup restoration flows.
- Email confirmation resend, password recovery/update, manual refresh, callback
  handling and scoped sign-out operations.
- Configurable Auth persistence, PKCE verifier storage, refresh behavior and
  lifecycle settings. PKCE is now the JchuComponents default.
- PostgREST RPC helpers and Edge Function options for custom headers,
  idempotency keys and correlation ids.

### Changed

- Supabase coroutine and JSON types used by the public API are exported as API
  dependencies.

### Fixed

- Auth callbacks now validate configured schemes and hosts
  case-insensitively and decode errors returned in implicit-flow fragments.
- Auth flows preserve coroutine cancellation and classify network failures as
  retryable typed errors.

## 3.0.0-alpha10

### Changed

- Reissued the `3.0.0-alpha09` component set under new coordinates after a
  GitHub Actions infrastructure incident prevented the Maven Central and
  SwiftPM publication jobs from acquiring hosted runners.
- No source or public API changes from `3.0.0-alpha09`.

## 3.0.0-alpha09

### Added

- Controlled Android and SwiftUI color pickers, preference rows and expandable
  text components.
- Android layouts, grouped input formatting, animated bottom sheets and iNook
  fidelity components with catalog coverage.
- SwiftUI requirements cards, amount counters and iNook fidelity counterparts.
- Android unit and instrumentation coverage for the new UI component families.

### Fixed

- Android and Apple preference factories now reuse one DataStore-backed
  `JchuPreferences` instance per absolute file path.
- The animated bottom sheet no longer emits a dismissal for an initially absent
  value or intercepts back navigation while hidden.
- Public requirement, dialog and expandable-description color and shape options
  now affect their rendered components.
- SwiftUI fidelity components use packaged system symbols for their internal
  icons instead of requiring undeclared iNook assets.

### Changed

- `JchuScrollableColumn`, `JchuPreferenceItem`, `JchuPreferenceChoice` and
  `JchuPreferenceSwitch` are the preferred Android APIs. Their previous public
  names remain available as deprecated compatibility wrappers.
- Supabase dependencies were updated from 3.6.0 to 3.7.0.

## 3.0.0-alpha08

### Added

- Kotlin Multiplatform Room and Supabase modules for Android and Apple targets.
- Maven BOM publication and macOS variants for supported KMP modules.
- Expanded Android previews and a redesigned native Swift component catalog.
- Additional Swift helpers, public payment models and consumer documentation.

### Fixed

- Maven Central and Swift release wiring for the new data modules and BOM.
- Swift date, numeric and frame-alignment APIs.

## 3.0.0-alpha07

### Added

- Aggregated Dokka documentation with automatic GitHub Pages deployment.
- Pastel Jeluchu documentation theme using the Visby typeface and project logo.
- Kotlin Multiplatform nullable Boolean, number and String extensions.
- Kotlinx-datetime bridges from Kotlin dates and instants to `NSDate`.
- Navigation 3 back-stack extensions for Compose applications.
- Swift extensions for concurrency, data URIs, image formats, collections and URLs.
- SwiftUI helpers for conditional modifiers, snapshots, rounded corners and image optimization.
- Tests covering the new KMP and Swift extension APIs.

### Changed

- iNook consumers now use JchuComponents for shared nullable, number, String,
  network-flow and navigation extensions.
- Dokka now aggregates every public Android and KMP module.
- SwiftPM declares its supported macOS deployment target explicitly.

### Distribution

- Android-only artifacts remain available from JitPack under
  `com.github.jeluchu.jchucomponents`.
- Kotlin Multiplatform artifacts are published to Maven Central under
  `io.github.jeluchu`.
- Swift releases are distributed through `Jeluchu/jchucomponents-spm`.
