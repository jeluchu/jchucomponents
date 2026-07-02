#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

required_android_patterns=(
  "enum class CatalogScenario"
  "LIGHT"
  "DARK"
  "ACCESSIBILITY"
  "CatalogFixtureKind.ENABLED"
  "CatalogFixtureKind.DISABLED"
  "CatalogFixtureKind.LOADING"
  "CatalogFixtureKind.ERROR"
  "CatalogFixtureKind.LONG_CONTENT"
)

required_swift_patterns=(
  "enum JchuCatalogScenario"
  "case light"
  "case dark"
  "case accessibility"
  "case enabled"
  "case disabled"
  "case loading"
  "case error"
  "case longContent"
)

for pattern in "${required_android_patterns[@]}"; do
  if ! grep -R --fixed-strings --quiet "$pattern" "$ROOT_DIR/app/src/main/kotlin/com/jeluchu/composer/core/catalog"; then
    echo "Missing Android catalog visual-readiness pattern: $pattern" >&2
    exit 1
  fi
done

for pattern in "${required_swift_patterns[@]}"; do
  if ! grep -R --fixed-strings --quiet "$pattern" "$ROOT_DIR/swift/Sources/JchuComponentsCatalog"; then
    echo "Missing Swift catalog visual-readiness pattern: $pattern" >&2
    exit 1
  fi
done

echo "Catalog visual-readiness scenarios and fixtures are present."
