#!/usr/bin/env bash
# Verifies, against the artifacts that publishToMavenLocal just wrote, that every Android AAR carries the
# resources its module declares. A KMP library built with com.android.kotlin.multiplatform.library only packs
# `res/` and the Compose resources (`assets/composeResources`) when `androidResources.enable = true`; without it the
# build and the tests pass, and the app crashes at runtime with a missing resource.
#
# Usage: scripts/check-published-android-resources.sh [version]   (run after publishToMavenLocal)
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

catalog_version="$(sed -n 's/^jchucomponents = "\(.*\)"/\1/p' gradle/libs.versions.toml)"
version="${1:-$catalog_version}"
repo="${HOME}/.m2/repository/io/github/jeluchu"
errors=0
checked=0

fail() {
  echo "ERROR: $*" >&2
  errors=$((errors + 1))
}

for dir in jchucomponents-*/; do
  module="${dir%/}"
  [[ -f "$module/build.gradle.kts" ]] || continue

  compose_resources=0
  [[ -n "$(find "$module/src" -type d -name composeResources 2>/dev/null | head -1)" ]] && compose_resources=1
  android_resources=0
  [[ -n "$(find "$module/src" -type f -path '*/res/*' 2>/dev/null | head -1)" ]] && android_resources=1
  ((compose_resources || android_resources)) || continue

  # Pure Android modules publish as <module>, KMP modules as <module>-android.
  aar=""
  for candidate in "$repo/$module-android/$version/$module-android-$version.aar" "$repo/$module/$version/$module-$version.aar"; do
    [[ -f "$candidate" ]] && aar="$candidate" && break
  done
  if [[ -z "$aar" ]]; then
    fail "$module: no AAR found in $repo for $version (run publishToMavenLocal first)"
    continue
  fi

  entries="$(unzip -l "$aar" | awk '{print $4}')"
  checked=$((checked + 1))
  if ((compose_resources)) && ! grep -q '^assets/composeResources/' <<<"$entries"; then
    fail "$module: has composeResources but its AAR has no assets/composeResources (set androidResources.enable = true)"
  fi
  if ((android_resources)) && ! grep -q '^res/' <<<"$entries"; then
    fail "$module: has Android res files but its AAR has no res/ (set androidResources.enable = true)"
  fi
done

if ((errors > 0)); then
  echo "Published Android resources check failed with $errors problem(s)." >&2
  exit 1
fi
echo "Published Android resources OK ($checked module(s) with resources checked for $version)."
