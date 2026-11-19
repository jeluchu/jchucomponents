#!/usr/bin/env bash
# Static release checks that need no build. Catches the mistakes that force a new version + tag:
#  - the tag/version is not the catalog version, or the tag already exists
#  - a publishable module is missing from the Maven Central release list, settings.gradle.kts or the BOM
#  - a release list entry points to a module that does not exist
# Usage: scripts/check-release-readiness.sh [--check-tag] [version]
#   --check-tag  also fail when the tag already exists (use it before tagging; CI on branches omits it)
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

check_tag=0
if [[ "${1:-}" == "--check-tag" ]]; then
  check_tag=1
  shift
fi

catalog_version="$(sed -n 's/^jchucomponents = "\(.*\)"/\1/p' gradle/libs.versions.toml)"
version="${1:-$catalog_version}"
errors=0

fail() {
  echo "ERROR: $*" >&2
  errors=$((errors + 1))
}

[[ -n "$catalog_version" ]] || fail "Cannot read the jchucomponents version from gradle/libs.versions.toml"
[[ "$version" == "$catalog_version" ]] || fail "Version '$version' does not match the catalog version '$catalog_version'"

if ((check_tag)); then
  if git rev-parse --verify --quiet "refs/tags/$version" >/dev/null; then
    fail "Tag '$version' already exists locally: a new version needs a new tag"
  fi
  if git ls-remote --exit-code --tags origin "refs/tags/$version" >/dev/null 2>&1; then
    fail "Tag '$version' already exists on origin: a new version needs a new tag"
  fi
fi

# Every module that applies the publish plugin.
publishable=()
for dir in jchucomponents-*/; do
  module="${dir%/}"
  build_file="$module/build.gradle.kts"
  if [[ -f "$build_file" ]] && grep -q 'jchucomponents.publish' "$build_file"; then
    publishable+=("$module")
  fi
done

for module in "${publishable[@]}" jchucomponents-bom; do
  grep -q "\":$module\"" settings.gradle.kts || fail "$module is not included in settings.gradle.kts"
  grep -q ":$module:publishAndReleaseToMavenCentral" .github/workflows/release.yml \
    || fail "$module is missing from the Maven Central list in .github/workflows/release.yml"
done

for module in "${publishable[@]}"; do
  grep -q "io.github.jeluchu:$module:" jchucomponents-bom/build.gradle.kts \
    || fail "$module is missing from jchucomponents-bom/build.gradle.kts"
done

# Release list entries must exist.
for entry in $(grep -o ':jchucomponents-[a-z0-9-]*:publishAndReleaseToMavenCentral' .github/workflows/release.yml); do
  module="${entry#:}"
  module="${module%%:*}"
  [[ -d "$module" ]] || fail "release.yml publishes '$module', which does not exist"
done

if ((errors > 0)); then
  echo "Release readiness failed with $errors problem(s)." >&2
  exit 1
fi
echo "Release readiness OK for $version (${#publishable[@]} publishable modules + BOM)."
