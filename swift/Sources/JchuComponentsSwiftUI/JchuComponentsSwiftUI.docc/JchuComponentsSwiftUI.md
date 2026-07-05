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

Choose controlled state when the parent needs to coordinate search expansion:

```swift
JchuExpandableSearch(
    query: $query,
    isExpanded: $isSearchExpanded,
    defaults: SearchBarDefaults(label: "Search")
) { expanded in
    analytics.trackSearch(expanded: expanded)
} onSearch: { query in
    search(query)
}
```

Use a growing field for multiline input with a hard character limit:

```swift
JchuGrowingTextField(
    value: $notes,
    defaults: GrowingTextFieldDefaults(
        label: "Notes",
        placeholder: "Add context…",
        maxCharacters: 200
    )
)
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

### Inputs

- ``JchuExpandableSearch``
- ``SearchBarDefaults``
- ``JchuGrowingTextField``
- ``GrowingTextFieldDefaults``
