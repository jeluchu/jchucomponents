# Changelog

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
