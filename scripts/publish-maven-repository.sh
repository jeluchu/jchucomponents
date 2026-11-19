#!/usr/bin/env bash
# Publishes every publishable module (and the BOM) as a plain Maven repository layout into <target-dir>,
# the same files Maven Central would receive, minus signatures and javadoc jars.
# Used by the "Publish KMP artifacts to the Maven repository" job in .github/workflows/release.yml, which
# copies the result into https://github.com/jeluchu/jchucomponents-maven.
# Usage: scripts/publish-maven-repository.sh <target-dir>
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

target="${1:?usage: scripts/publish-maven-repository.sh <target-dir>}"
mkdir -p "$target"
target="$(cd "$target" && pwd)"
stage="$(mktemp -d)"
trap 'rm -rf "$stage"' EXIT

tasks=(":jchucomponents-bom:publishToMavenLocal")
for dir in jchucomponents-*/; do
  module="${dir%/}"
  build_file="$module/build.gradle.kts"
  if [[ -f "$build_file" ]] && grep -q 'jchucomponents.publish' "$build_file"; then
    tasks+=(":$module:publishToMavenLocal")
  fi
done

# maven.repo.local redirects publishToMavenLocal so ~/.m2 is left untouched.
./gradlew -Dmaven.repo.local="$stage" "${tasks[@]}"

# Javadoc jars are ~85% of the size and only Maven Central requires them; Gradle never downloads them.
find "$stage" -name '*-javadoc.jar*' -delete
# Local publication metadata that has no meaning in a hosted repository.
find "$stage" -name 'maven-metadata-local.xml*' -delete

rsync -a "$stage"/ "$target"/
echo "Published ${#tasks[@]} modules to $target"
