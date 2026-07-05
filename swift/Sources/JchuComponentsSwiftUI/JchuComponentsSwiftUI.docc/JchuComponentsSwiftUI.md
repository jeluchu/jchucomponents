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

Provide a theme once at the root of a feature or application:

```swift
let theme = JchuTheme(
    colors: JchuColors(
        primary: .mint,
        error: .orange
    ),
    spacing: JchuSpacing(dimen16: 18),
    shapes: JchuShapes(corner16: 20),
    motion: JchuMotion(durationMedium: 0.3)
)

ContentView()
    .jchuTheme(theme)
```

Use a preset to downsample images near their rendered size:

```swift
JchuNetworkImage(
    urlString: product.imageURL,
    configuration: .galleryThumbnail(
        size: CGSize(width: 160, height: 160)
    )
)
.frame(width: 160, height: 160)
```

Warm the cache before presenting an image-heavy destination:

```swift
@StateObject private var prefetcher = JchuNetworkImagePrefetcher()

prefetcher.prefetch(
    urlStrings: products.map(\.imageURL),
    configuration: .poster(size: CGSize(width: 240, height: 360))
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

### Theme

- ``JchuTheme``
- ``JchuColors``
- ``JchuSpacing``
- ``JchuShapes``
- ``JchuTypography``
- ``JchuMotion``

### Images

- ``JchuNetworkImage``
- ``JchuNetworkImageConfiguration``
- ``JchuNetworkImagePrefetcher``
- ``JchuNetworkImagePlaceholder``
- ``JchuNetworkImageErrorView``
