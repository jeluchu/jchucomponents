#!/usr/bin/env bash

set -euo pipefail

readonly project_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
readonly version_catalog="${project_root}/gradle/libs.versions.toml"
readonly version="$(
    sed -n 's/^jchucomponents = "\(.*\)"/\1/p' "${version_catalog}"
)"
readonly framework_name="JchuComponentsCore"
readonly framework_dir="${project_root}/jchucomponents-foundation/build/XCFrameworks/release/${framework_name}.xcframework"
readonly output_dir="${project_root}/build/swift-release/${version}"
readonly archive_path="${output_dir}/${framework_name}.xcframework.zip"
readonly manifest_template="${project_root}/swift/Package.release.swift.template"
readonly manifest_path="${output_dir}/Package.swift"
readonly package_sources="${output_dir}/Sources"
readonly package_tests="${output_dir}/Tests"

if [[ -z "${version}" ]]; then
    echo "Unable to read jchucomponents version from ${version_catalog}" >&2
    exit 1
fi

rm -rf "${output_dir}"
mkdir -p "${output_dir}"

"${project_root}/gradlew" \
    :jchucomponents-foundation:assembleJchuComponentsCoreReleaseXCFramework

ditto -c -k --sequesterRsrc --keepParent \
    "${framework_dir}" \
    "${archive_path}"

readonly checksum="$(swift package compute-checksum "${archive_path}")"

sed \
    -e "s/{{VERSION}}/${version}/g" \
    -e "s/{{CHECKSUM}}/${checksum}/g" \
    "${manifest_template}" > "${manifest_path}"

ditto \
    "${project_root}/swift/Sources/JchuComponentsExtensions" \
    "${package_sources}/JchuComponentsExtensions"
ditto \
    "${project_root}/swift/Sources/JchuComponentsSwiftUI" \
    "${package_sources}/JchuComponentsSwiftUI"
ditto \
    "${project_root}/swift/Sources/JchuComponentsPay" \
    "${package_sources}/JchuComponentsPay"
ditto \
    "${project_root}/swift/Tests/JchuComponentsSwiftUITests" \
    "${package_tests}/JchuComponentsSwiftUITests"

echo "Swift release prepared:"
echo "  Version:  ${version}"
echo "  Archive:  ${archive_path}"
echo "  Checksum: ${checksum}"
echo "  Manifest: ${manifest_path}"
echo "  Sources:  ${package_sources}"
