#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

required_paths=(
  "$ROOT_DIR/jchucomponents-foundation/src/commonMain"
  "$ROOT_DIR/jchucomponents-network/src/commonMain"
  "$ROOT_DIR/jchucomponents-core/src/main"
)

required_patterns=(
  "JchuProgressButtonState"
  "JchuProgressState"
  "package com.jeluchu.jchucomponents.network"
  "package com.jeluchu.jchucomponents.network.resource"
)

for path in "${required_paths[@]}"; do
  if [[ ! -e "$path" ]]; then
    echo "Missing tracked API readiness path: $path" >&2
    exit 1
  fi
done

for pattern in "${required_patterns[@]}"; do
  if ! grep -R --fixed-strings --quiet "$pattern" \
    "$ROOT_DIR/jchucomponents-foundation/src/commonMain" \
    "$ROOT_DIR/jchucomponents-network/src/commonMain" \
    "$ROOT_DIR/jchucomponents-core/src/main"; then
    echo "Missing API readiness pattern: $pattern" >&2
    exit 1
  fi
done

echo "Tracked API readiness sources are present."
