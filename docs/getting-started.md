# Consumer guide

JchuComponents publishes Kotlin Multiplatform libraries, Android-only
libraries and a native Swift package. All artifacts from one release use the
same version.

## Kotlin Multiplatform

KMP modules are available from the `jchucomponents-maven` repository (`https://raw.githubusercontent.com/jeluchu/jchucomponents-maven/main`) under `io.github.jeluchu`.
Declare only the capabilities used by the application:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("io.github.jeluchu:jchucomponents-foundation:3.0.0-beta02")
            implementation("io.github.jeluchu:jchucomponents-network:3.0.0-beta02")
            implementation("io.github.jeluchu:jchucomponents-ui:3.0.0-beta02")
            implementation("io.github.jeluchu:jchucomponents-prefs:3.0.0-beta02")
            implementation("io.github.jeluchu:jchucomponents-supabase:3.0.0-beta02")
        }
    }
}
```

Available modules:

| Artifact | Platforms | Use it for |
| --- | --- | --- |
| `jchucomponents-foundation` | Android, iOS ARM64, macOS ARM64 | Portable state, date, text and version helpers |
| `jchucomponents-network` | Android, iOS ARM64, macOS ARM64 | Ktor configuration, resources and flow helpers |
| `jchucomponents-pay` | Android, iOS ARM64 | RevenueCat purchases and subscription models |
| `jchucomponents-prefs` | Android, iOS ARM64, macOS ARM64 | Typed observable preferences |
| `jchucomponents-qr` | Android, iOS ARM64, macOS ARM64 | QR encoding and decoding |
| `jchucomponents-supabase` | Android, iOS ARM64, macOS ARM64 | Supabase Auth, PostgREST, RPC, Realtime and Edge Functions |
| `jchucomponents-ui` | Android, iOS ARM64, JVM | Compose Multiplatform components, with Android-specific extras on Android |

`jchucomponents-ui` is a single multiplatform artifact: Android applications also
receive the components that depend on Android resources, `Context`, `View`,
`Bitmap` or Android Coil APIs.

## Android

The BOM is published to the same repository and aligns every JchuComponents module:

```kotlin
dependencies {
    implementation(
        platform("io.github.jeluchu:jchucomponents-bom:3.0.0-beta02")
    )

    implementation("io.github.jeluchu:jchucomponents-foundation")
    implementation("io.github.jeluchu:jchucomponents-ui")
    implementation("io.github.jeluchu:jchucomponents-android")
}
```

All modules are resolved from the `jchucomponents-maven` repository. JitPack builds each
tag too, but it is not required and it cannot serve the Apple variants of KMP modules.

## Swift

Add `https://github.com/Jeluchu/jchucomponents-spm` in Xcode and select the
`JchuComponents` product:

```swift
import JchuComponentsCore
import JchuComponentsExtensions
import JchuComponentsSwiftUI

struct ContentView: View {
    var body: some View {
        JchuProgressButton("Continue") {
            // Handle the action.
        }
    }
}
```

Select the optional `JchuComponentsPay` product only when the application
needs the native RevenueCat integration.

## Verifying a local checkout

Run all Android and Kotlin checks through the root lifecycle task:

```bash
./gradlew ciCheck
```

On macOS, assemble the binary framework and build the Swift package for iOS:

```bash
./gradlew \
  :jchucomponents-foundation:assembleJchuComponentsCoreReleaseXCFramework

xcodebuild \
  -scheme JchuComponents-Package \
  -destination 'generic/platform=iOS Simulator' \
  build
```

Compile an external KMP consumer for macOS after publishing the modules to
Maven Local:

```bash
./gradlew \
  -p smoke-tests/kmp-consumer \
  compileKotlinMacosArm64
```
