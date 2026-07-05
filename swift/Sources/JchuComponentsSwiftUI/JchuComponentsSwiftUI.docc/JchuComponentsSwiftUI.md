# ``JchuComponentsSwiftUI``

Build native SwiftUI interfaces with JchuComponents views and shared Kotlin
Multiplatform state.

## Overview

JchuComponentsSwiftUI provides native views that can be configured with Swift
values or, where behavior is shared across platforms, with models from
`JchuComponentsCore`.

Use a progress button to prevent duplicate actions while work is running:

```swift
JchuProgressButton(
    "Continue",
    isLoading: isSubmitting
) {
    submit()
}
```

Progress views accept native values:

```swift
JchuLinearProgress(
    "Downloading",
    value: downloadedBytes,
    maxValue: totalBytes
)
```

They can also consume shared state directly:

```swift
JchuCircularProgress(state: sharedProgressState)
```

## Topics

### Actions

- ``JchuProgressButton``
- ``JchuChip``

### Loading and progress

- ``JchuLoadingIndicator``
- ``JchuLinearProgress``
- ``JchuCircularProgress``
- ``JchuIconProgress``
