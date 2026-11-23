# Changelog

## 3.0.0-beta02 - 2026-11-23

### Fixed

- `jchucomponents-pdf` now packs its Android resources: the Compose strings of the reader (`assets/composeResources`) and
  the `FileProvider` paths (`res/xml`) were missing from the Android AAR, so opening the PDF screen from an app failed at
  runtime with a missing resource. Present in `3.0.0-beta01`.
- The documentation build no longer looks for the logo in the old `jchucomponents-ui/src/main/res` path.
- `rememberJchuZoomState` no longer recreates its state on every recomposition, which reset the zoom to 1x right after a
  double tap or pinch. `velocityDecay` is no longer a `remember` key.

### Improvements

- The PDF reader uses a full-screen page with floating rounded controls (back, `1 de 32` page pill, share, rotate and
  previous/next), keeps zoom and pan inside the page, and renders the Android page at 2x so it stays sharp when zoomed.
  The Spanish page indicator reads `1 de 32`.
- `scripts/check-published-android-resources.sh` runs in the release preflight and in CI and fails when a published
  Android AAR is missing the resources its module declares.

## 3.0.0-beta01 - 2026-11-22

First beta. The module layout and the Maven coordinates are final; smaller API cleanups may still follow in the next betas.
The sections below also collect the changes of `3.0.0-alpha16` to `3.0.0-alpha35`, which never had an entry of their own.

### Breaking changes

The module layout was consolidated before `3.0.0-beta01`. See
[docs/migration-v3-modules.md](docs/migration-v3-modules.md) for the full map and import rewrites.

- Every module is now published under `io.github.jeluchu` from the `jchucomponents-maven` repository.
  Applications should use that repository; the `com.github.jeluchu...` JitPack coordinates are no longer
  the supported way to consume the library (JitPack still builds each tag).
- `jchucomponents-core`, `jchucomponents-ktx` and `jchucomponents-app-updates` were removed. Their Android
  code is now `jchucomponents-android` (Android-only; Kotlin packages unchanged, `R` moved to
  `com.jeluchu.jchucomponents.android.R`). Their portable code moved to `jchucomponents-foundation`:
  `States`/`ListStates` (`foundation.state`), UUID and random helpers (`foundation.random`),
  `noCrash`/`doOn*`/`cancelIfActive` (`foundation.coroutines`). `foundation` now depends on
  `kotlinx-coroutines-core`, and `doOnMain` runs on `Dispatchers.Default`.
- `jchucomponents-ui-kmp`, `jchucomponents-pay-kmp` and `jchucomponents-pdf-kmp` were renamed to
  `jchucomponents-ui`, `jchucomponents-pay` and `jchucomponents-pdf`. The Android-only `ui` code now lives in
  the `androidMain` of `jchucomponents-ui`. Packages `com.jeluchu.pay.revenuecat.kmp` and
  `com.jeluchu.jchucomponents.pdf.kmp` became `com.jeluchu.pay.revenuecat` and `com.jeluchu.jchucomponents.pdf`.
- `jchucomponents-navigation3-di` was merged into `jchucomponents-navigation3` (package `navigation3.di` unchanged);
  `navigation3` now depends on Koin Compose.
- The legacy Android `jchucomponents-pay` (Play Billing) and `jchucomponents-pdf` modules were removed.
- `JchuLegacyExpandableDescription` and `JchuLegacySimpleDescription` were removed.
- `jchucomponents-ui` is Material 3 only: it no longer exposes Material 2 (`compose.material`) and declares
  `org.jetbrains.compose.material3:material3`. `ScaffoldStates` and `SnackbarController` take a
  `SnackbarHostState` instead of `ScaffoldState`, the Material 2 `Snackbar` copy was removed and
  `ui.theme.typography` is now a Material 3 `Typography`. Applications that still use Material 2 must declare it.

### Improvements

- Portable extensions moved out of `jchucomponents-android` into the KMP modules: number helpers (`orEmpty`, `isNotEmpty`,
  `thousandsFormat`, `milliSecondsToTimer`...) and text helpers (`getLastBitFromUrl`, `extractYTId`, `isIp`...) in
  `foundation`, and `Color` helpers (`orTransparent`, `toColorFilter`, `isDark`...) in `ui`. See the migration guide.
- `runCatchingCancellable { }` in `foundation.coroutines`: `runCatching` that rethrows `CancellationException`, so a cancelled
  coroutine stops instead of continuing after the failure was swallowed. `runCatchingExceptions { }` is the same but only captures
  `Exception`s, so `Error`s keep propagating like with `catch (e: Exception)`. `noCrashSuspend` now uses `runCatchingCancellable`.
- `network` gets `Flow<Resource>` await helpers (`awaitResource`, `awaitResourceData`, `awaitRequiredResourceData`,
  `awaitResourceUnit`, `asResourceFlow`, `catchAsResourceError`) and `ResourceException`; `foundation` gets
  `Throwable.rethrowIfCancellation()`, a public `String.normalizeForSearch()` and `NSData.toByteArray()`.

- `PriceUtil` (divide/multiply formatted prices) is available in the multiplatform `jchucomponents-pay`.
- The BOM lists only the 13 current modules.
- `jchucomponents-ui` and `jchucomponents-pdf` are published to the Maven repository with every other module.

### Added

- Extensions shared by the apps, so they can drop their own copies: `DrawableResource.toPainter()`,
  `toImageVector()` and `StringResource.toStringRes(vararg)` in `jchucomponents-ui-kmp` (which now exposes
  `compose.components.resources`); `NSDate.toKotlinInstant/LocalDateTime/LocalDate`, `LocalDate.formatForSwift` and
  `ByteArray.toNSData()` for Apple targets, and `List<T>.search(query, selector)` (case and accent insensitive),
  `String.isNotZero()`, `String.isBuyNotAvailability()` and `String.getTranslateOrDefault()` in `jchucomponents-foundation`.
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
- `crashlytics { }` now sets `app_platform`, `app_version` and `app_build` on its own (`appContext`), and
  `crashlytics { userId { flow } }` / `analytics { userId { flow } }` keep the user id in sync with a flow, so apps no
  longer set them by hand. `JchuFeature.onStarted(platform, koin)` runs after Koin starts.
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

- `extractYTId` now reads `youtube.com/watch?v=ID` URLs (the old regex treated `?` as a quantifier), and `milliSecondsToTimer`
  pads the minutes when there are hours (`1:01:05` instead of `1:1:05`).
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
