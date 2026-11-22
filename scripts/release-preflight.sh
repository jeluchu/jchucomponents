#!/usr/bin/env bash
# Runs, before any tag is pushed, the same verification the Release workflow runs, so a broken
# release is found without burning a version and a tag.
#
#   scripts/release-preflight.sh [version]      # locally (macOS runs the Apple steps too)
#   gh workflow run "Release preflight"         # on GitHub, same script on a macOS runner
#
# The Release workflow calls this script as its first step, so both stay identical.
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

# Nested builds (smoke-tests) have no local.properties of their own: reuse the root SDK location.
if [[ -z "${ANDROID_HOME:-}" && -f local.properties ]]; then
  sdk_dir="$(sed -n 's/^sdk.dir=//p' local.properties)"
  [[ -n "$sdk_dir" ]] && export ANDROID_HOME="$sdk_dir"
fi

catalog_version="$(sed -n 's/^jchucomponents = "\(.*\)"/\1/p' gradle/libs.versions.toml)"
version="${1:-$catalog_version}"

step() { printf '\n==> %s\n' "$*"; }

step "Static release checks"
if [[ "${GITHUB_REF_TYPE:-}" == "tag" ]]; then
  scripts/check-release-readiness.sh "$version"          # the tag being released already exists
else
  scripts/check-release-readiness.sh --check-tag "$version"
fi
scripts/check-visual-readiness.sh
scripts/check-api-readiness.sh

step "Build, tests, API checks and Maven Local publications"
./gradlew ciCheck -x :app:assembleRelease

if [[ "$(uname)" == "Darwin" ]]; then
  step "Kotlin tests on Apple targets"
  ./gradlew \
    :jchucomponents-config:iosSimulatorArm64Test \
    :jchucomponents-foundation:iosSimulatorArm64Test \
    :jchucomponents-monitoring:iosSimulatorArm64Test \
    :jchucomponents-network:iosSimulatorArm64Test \
    :jchucomponents-pay:iosSimulatorArm64Test \
    :jchucomponents-prefs:iosSimulatorArm64Test \
    :jchucomponents-qr:iosSimulatorArm64Test \
    :jchucomponents-room:iosSimulatorArm64Test \
    :jchucomponents-supabase:iosSimulatorArm64Test \
    :jchucomponents-config:macosArm64Test \
    :jchucomponents-foundation:macosArm64Test \
    :jchucomponents-monitoring:macosArm64Test \
    :jchucomponents-network:macosArm64Test \
    :jchucomponents-prefs:macosArm64Test \
    :jchucomponents-qr:macosArm64Test \
    :jchucomponents-room:macosArm64Test \
    :jchucomponents-supabase:macosArm64Test

  step "Published KMP consumer (Android, iOS, macOS)"
  ./gradlew -p smoke-tests/kmp-consumer compileAndroidMain compileKotlinIosSimulatorArm64 compileKotlinMacosArm64

  step "Swift release payload"
  scripts/prepare-swift-release.sh
  swift package dump-package --package-path "build/swift-release/${version}" >/dev/null
  test -f "build/swift-release/${version}/Plugins/JchuComponentsNavigationGraphPlugin/plugin.swift"
else
  echo "Skipping Apple and Swift steps: not running on macOS."
fi

step "Release preflight passed for ${version}"
