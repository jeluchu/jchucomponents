# Migrating to the v3 module layout

`3.0.0-beta01` consolidates the module structure that the alphas grew. Every module is now
published under `io.github.jeluchu` from the `jchucomponents-maven` repository, which is the
source to use in applications. JitPack still builds each tag, but the `com.github.jeluchu...`
coordinates are no longer the supported way to consume the library.

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://raw.githubusercontent.com/jeluchu/jchucomponents-maven/main")
    }
}
```

Migrating from `3.0.0-alpha35` is a dependency-coordinate change plus a few import rewrites.
Behavior is unchanged except where noted.

## Module map

| Before (alpha35) | Now | Notes |
|---|---|---|
| `jchucomponents-core` (`io.github` or JitPack) | removed | portable code moved to `foundation`, Android code to `android` |
| `jchucomponents-ktx` (`io.github` or JitPack) | `jchucomponents-android` | portable helpers moved to `foundation` |
| `jchucomponents-app-updates` | `jchucomponents-android` | package `appupdates` unchanged |
| `jchucomponents-ui-kmp` | `jchucomponents-ui` | same packages |
| `jchucomponents-ui` (JitPack, Android-only) | `jchucomponents-ui` | its code now lives in `androidMain` of the same artifact |
| `jchucomponents-pay-kmp` | `jchucomponents-pay` | package changed, see below |
| `jchucomponents-pay` (legacy Android) | removed | use the KMP API |
| `jchucomponents-pdf-kmp` | `jchucomponents-pdf` | package changed, see below |
| `jchucomponents-pdf` (legacy Android) | removed | use the KMP API |
| `jchucomponents-navigation3-di` | `jchucomponents-navigation3` | same package `navigation3.di`; Koin Compose is now a dependency of `navigation3` |

Unchanged modules: `foundation`, `network`, `config`, `prefs`, `room`, `supabase`, `monitoring`,
`qr`, `navigation3` and the BOM.

`jchucomponents-android` is **Android-only**: declare it in `androidMain` or in the Android
application module, never in `commonMain`.

## Import rewrites

### Moved to `jchucomponents-foundation`

| Old import | New import |
|---|---|
| `core.states.States`, `core.states.ListStates` | `foundation.state.States`, `foundation.state.ListStates` |
| `core.functional.Either` | `foundation.functional.Either` |
| `utils.getRandomUUID`, `getUUIDBits`, `randInt`, `randDouble` | `foundation.random.*` |
| `utils.coroutines.*`, `ktx.coroutines.*` | `foundation.coroutines.*` |
| `ktx.job.cancelIfActive` | `foundation.coroutines.cancelIfActive` |
| `extensions.commons.tryOrNull`, `tryOrDefault`, `safeLet` | `foundation.result.*` |
| `utils.location.LocationUtils` | `foundation.location.LocationUtils` |
| `utils.validators.CreditCardValidator` | `foundation.validators.CreditCardValidator` |
| `ktx.bool.orFalse` | `foundation.extensions.orFalse` |
| `ktx.lists.*` | `foundation.extensions.lists.*` |
| `ktx.serialization.*` | `foundation.serialization.*` (`toMap` is now `toJsonObject`; the shared `json` is `defaultJson`) |

All of these start with `com.jeluchu.jchucomponents.`.

Behavior note: `doOnMain` in `foundation.coroutines` now runs on `Dispatchers.Default`
(`Dispatchers.IO` is not available in common code). Wrap the call in `withContext(Dispatchers.IO)`
if you relied on the IO pool.

### Payments (`jchucomponents-pay`)

```diff
-import com.jeluchu.pay.revenuecat.kmp.Payment
+import com.jeluchu.pay.revenuecat.Payment
```

Every `com.jeluchu.pay.revenuecat.kmp.*` import becomes `com.jeluchu.pay.revenuecat.*`.
`PriceUtil` is available at `com.jeluchu.pay.revenuecat.utils.PriceUtil`.

### PDF (`jchucomponents-pdf`)

```diff
-import com.jeluchu.jchucomponents.pdf.kmp.JchuPdfReaderScreen
+import com.jeluchu.jchucomponents.pdf.JchuPdfReaderScreen
```

The generated navigation names (`pdfReaderKmpEntries`, `PdfReaderKmpRoutes`) are unchanged.

### Android resources

The `R` class of the former `ktx` module moved with the new namespace:
`com.jeluchu.jchucomponents.ktx.R` is now `com.jeluchu.jchucomponents.android.R`.

## Extensions that moved from `jchucomponents-android` to the KMP modules

The portable extensions of the old `ktx` package now live in KMP modules, so they work in `commonMain`:

| Old import | New import |
|---|---|
| `ktx.strings.empty`, `ktx.numbers.empty` | `foundation.extensions.empty` |
| `ktx.strings.orEmpty`, `ktx.numbers.orEmpty`, `ktx.numbers.isNotEmpty` | `foundation.extensions.orEmpty`, `isNotEmpty` |
| `ktx.numbers.bytesToMeg`, `milliSecondsToTimer`, `roundUpToNearestTen`, `thousandsFormat` | `foundation.extensions.*` |
| `ktx.any.isNull` | `foundation.extensions.isNull` |
| `ktx.strings.getLastBitFromUrl`, `extractYTId`, `deleteDouble`, `safeValue`, `removeCharactersWeirds`, `isEmptyString`, `isIp`, `getCurrencyString` | `foundation.text.*` |
| `ktx.colors.orTransparent`, `opacity`, `isDark`, `toColorFilter`, `random` | `ui.extensions.color.*` |

All prefixes are `com.jeluchu.jchucomponents.`. `ktx.strings.isEmpty` (a copy of the stdlib function) and
`Color.Companion.Random` were removed.

Behavior changes while moving:

- `extractYTId` now recognizes `youtube.com/watch?v=ID` URLs (the old pattern treated `?` as a quantifier and only matched
  `youtu.be` and `embed` links).
- `milliSecondsToTimer` pads the minutes when there are hours (`1:01:05`, it used to return `1:1:05`).
- `Color.isDark()` uses Compose's `luminance()` instead of `androidx.core.ColorUtils`.

Not moved on purpose: `ktx.strings.noNumbers`, `onlyNumbers`, `allUpperCase`, `allLowerCase`, `atLeastOneNumber`,
`atLeastOneLowerCase`, `atLeastOneUpperCase`, `atLeastOneSpecialCharacter`, `noSpecialCharacter` and `startsWithNonNumber`.
They return `true` when the rule is *violated* (`noNumbers()` is `true` for a text that has digits), which would be a
misleading public KMP API. The `String` helpers whose names also exist in `foundation.text` but behave differently
(`capitalizeWords`, `capitalizeFirstLetter`, `toHttpsUrl`, `wordCount`, `isNumber`, `onlyDigits`) stay in
`jchucomponents-android`; prefer the `foundation.text` versions.

New in this release: `runCatchingCancellable` (use it instead of `runCatching` around suspend calls, and instead of
`try { } catch (e: CancellationException) { throw e } catch (e: Throwable) { }`; `runCatchingExceptions` replaces the same block with `catch (e: Exception)`), `Flow<Resource>.awaitResource`, `awaitResourceData`, `awaitRequiredResourceData`, `awaitResourceUnit`,
`asResourceFlow`, `catchAsResourceError` and `ResourceException` in `network`; `Throwable.rethrowIfCancellation` in
`foundation.coroutines`; a public `String.normalizeForSearch()` and `NSData.toByteArray()` in `foundation`.

## Material 3 only

`jchucomponents-ui` no longer depends on Material 2. It declares
`org.jetbrains.compose.material3:material3` (on Android that artifact resolves to
`androidx.compose.material3:material3`), and the library catalog no longer lists any
`androidx.compose.material` or `androidx.compose.material3` dependency.

- Applications that still import `androidx.compose.material.*` (anything other than
  `material.icons`) used to receive Material 2 through `jchucomponents-ui`. Declare it yourself, for
  example `implementation(compose.material)`. `BackdropScaffold` has no Material 3 equivalent.
- `ScaffoldStates(scaffoldState, backgroundColor, isFloatingActionButtonDocked, ...)` is now
  `ScaffoldStates(snackbarHostState, containerColor, ...)`.
- `SnackbarController.showSnackbar` and `showSnack` take a `SnackbarHostState` instead of a
  `ScaffoldState` or `BottomSheetScaffoldState`; the class is no longer `@ExperimentalMaterialApi`.
- The `Snackbar` and `CustomSnackbar` copies of the Material 2 snackbar were removed; use
  `androidx.compose.material3.Snackbar`. `SnackbarCustomized` keeps its parameters and renders a Material 3
  snackbar (no `elevation`).
- `com.jeluchu.jchucomponents.ui.theme.typography` is a Material 3 `Typography`
  (`bodyLarge`, `bodyMedium`, `labelLarge`, `bodySmall`, `titleMedium`, `titleSmall`).
- `ScrollableChipTabRow` defaults to the Material 3 tab row container color; `ChipTab` no longer
  uses `LocalContentAlpha`; `Tooltip`, `TimeCard`, `ReadingIndicator`, `DebutCard` and `CategoryCard` read
  `MaterialTheme.colorScheme` and the Material 3 typography.

## Removed APIs

- `JchuLegacyExpandableDescription` and `JchuLegacySimpleDescription` (and their colors class).
- The legacy Android `jchucomponents-pay` (Play Billing based) and `jchucomponents-pdf` modules.
- The duplicated copies of `Either`, `LocationUtils`, `CreditCardValidator`, `MutableListExtensions`,
  `SerializationExtensions` and `BooleanExtensions` that lived in `core` and `ktx`.

## Version catalog example

```toml
[versions]
jchucomponents = "3.0.0-beta03"

[libraries]
jchucomponents-bom = { module = "io.github.jeluchu:jchucomponents-bom", version.ref = "jchucomponents" }
jchucomponents-foundation = { module = "io.github.jeluchu:jchucomponents-foundation" }
jchucomponents-ui = { module = "io.github.jeluchu:jchucomponents-ui" }
jchucomponents-pay = { module = "io.github.jeluchu:jchucomponents-pay" }
jchucomponents-android = { module = "io.github.jeluchu:jchucomponents-android" }
```

Put `platform(libs.jchucomponents.bom)` in the module that declares them and drop the individual
versions, or keep `version.ref` on each entry.
