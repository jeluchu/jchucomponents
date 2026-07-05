# ``JchuComponentsExtensions``

Use focused native Swift conveniences for collections, Foundation values and
concurrency.

## Overview

Helpers use one of two styles:

- Direct extensions when the name is specific and unlikely to collide.
- The `.jchu` namespace when a common name could conflict with another API.

Safely access a collection without assuming an integer index:

```swift
let item = items.jchu[safe: items.startIndex]
let metadata = valuesByKey.jchu[safe: valuesByKey.startIndex]
```

Append unique elements while retaining order:

```swift
var identifiers = [1, 2]
identifiers.addAllIfNotExist([2, 3])
// [1, 2, 3]
```

Encode and decode small Codable values:

```swift
let json = settings.toJson()
let settings = json?.fromJson(Settings.self)
```

## Topics

### Namespace

- ``JchuCompatible``
- ``JchuExtension``

### Collections

- ``JchuExtension/isNotEmpty``
- ``JchuExtension/subscript(safe:)``
- ``Swift/Array/addAllIfNotExist(_:)``
- ``Swift/Sequence/chunked(into:)``

### Codable

- ``Swift/Encodable/toJson(encoder:)``
- ``Swift/String/fromJson(_:decoder:)``
