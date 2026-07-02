#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

required_files=(
  "$ROOT_DIR/docs/api-compatibility.md"
  "$ROOT_DIR/docs/public-api-conventions.md"
  "$ROOT_DIR/docs/android-public-api-inventory.md"
)

required_patterns=(
  "Jchu<Component>"
  "apiCheck"
  "Swift symbol graphs"
  "Android public API inventory"
  "Existing Android API names remain source-compatible through"
)

for file in "${required_files[@]}"; do
  if [[ ! -f "$file" ]]; then
    echo "Missing API readiness file: $file" >&2
    exit 1
  fi
done

for pattern in "${required_patterns[@]}"; do
  if ! grep -R --fixed-strings --quiet "$pattern" "$ROOT_DIR/docs"; then
    echo "Missing API readiness pattern: $pattern" >&2
    exit 1
  fi
done

echo "API readiness documentation is present."
