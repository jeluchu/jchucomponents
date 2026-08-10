# Consumer guide

JchuComponents publishes Kotlin Multiplatform libraries, Android-only
libraries and a native Swift package. All artifacts from one release use the
same version.

## Kotlin Multiplatform

KMP modules are available from Maven Central under `io.github.jeluchu`.
Declare only the capabilities used by the application:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("io.github.jeluchu:jchucomponents-foundation:3.0.0-alpha10")
            implementation("io.github.jeluchu:jchucomponents-network:3.0.0-alpha10")
            implementation("io.github.jeluchu:jchucomponents-prefs:3.0.0-alpha10")
        }
    }
}
```

Available modules:

| Artifact | Platforms | Use it for |
| --- | --- | --- |
| `jchucomponents-foundation` | Android, iOS, macOS | Portable state, date, text and version helpers |
| `jchucomponents-network` | Android, iOS, macOS | Ktor configuration, resources and flow helpers |
| `jchucomponents-pay` | Android, iOS, macOS | Shared payment and subscription models |
| `jchucomponents-prefs` | Android, iOS, macOS | Typed observable preferences |
| `jchucomponents-qr` | Android, iOS, macOS | QR encoding and decoding |

## Android

The BOM is published to Maven Central and aligns both KMP and Android
JchuComponents versions:

```kotlin
dependencies {
    implementation(
        platform("io.github.jeluchu:jchucomponents-bom:3.0.0-alpha10")
    )

    implementation("io.github.jeluchu:jchucomponents-foundation")
    implementation("com.github.jeluchu.jchucomponents:jchucomponents-ui")
    implementation("com.github.jeluchu.jchucomponents:jchucomponents-ktx")
}
```

Android-only modules are resolved from JitPack, so applications using them
must also add `https://jitpack.io` to dependency repositories.

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
