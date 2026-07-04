#!/usr/bin/env python3
"""Synchronize Android string resources into an Apple String Catalog."""

import argparse
import json
import re
import sys
from pathlib import Path
from typing import Optional
from xml.etree import ElementTree as ET


ANDROID_TO_APPLE = {
    "ar": "ar",
    "bg": "bg",
    "ca": "ca",
    "cs": "cs",
    "da": "da",
    "de": "de",
    "el": "el",
    "en": "en",
    "es": "es",
    "et": "et",
    "eu": "eu",
    "fi": "fi",
    "fr": "fr",
    "he": "he",
    "hr": "hr",
    "hu": "hu",
    "id": "id",
    "it": "it",
    "ja": "ja",
    "ko": "ko",
    "lt": "lt",
    "lv": "lv",
    "ms": "ms",
    "nb": "nb",
    "nl": "nl",
    "pl": "pl",
    "pt": "pt-PT",
    "pt-BR": "pt-BR",
    "ro": "ro",
    "ru": "ru",
    "sk": "sk",
    "sv": "sv",
    "th": "th",
    "tr": "tr",
    "uk": "uk",
    "vi": "vi",
    "zh": "zh-Hans",
    "zh-TW": "zh-Hant",
}


def convert_format_specifiers(value: str) -> str:
    """Convert Android string placeholders to their Foundation equivalents."""
    value = re.sub(r"%(\d+\$)[sS]", r"%\1@", value)
    value = re.sub(r"%(?!\d)[sS]", "%@", value)
    return value.replace("\\'", "'")


def parse_strings_xml(xml_path: Path) -> dict[str, str]:
    try:
        root = ET.parse(xml_path).getroot()
    except ET.ParseError as error:
        print(f"warning: could not parse {xml_path}: {error}", file=sys.stderr)
        return {}

    strings = {}
    for element in root.findall("string"):
        name = element.get("name")
        if not name or element.get("translatable", "true") == "false":
            continue
        value = "".join(element.itertext())
        if value:
            strings[name] = convert_format_specifiers(value)
    return strings


def android_locale(directory_name: str, source_language: str) -> Optional[str]:
    if directory_name == "values":
        return source_language
    if not directory_name.startswith("values-"):
        return None

    qualifier = directory_name.removeprefix("values-")
    # BCP-47 resource qualifiers use values-b+es+419.
    if qualifier.startswith("b+"):
        qualifier = qualifier.removeprefix("b+").replace("+", "-")
    else:
        qualifier = qualifier.replace("-r", "-")
    return ANDROID_TO_APPLE.get(qualifier, qualifier)


def discover_android_strings(
    res_dir: Path, source_language: str
) -> list[tuple[str, dict[str, str]]]:
    result = []
    for directory in sorted(res_dir.iterdir()):
        xml_path = directory / "strings.xml"
        if not directory.is_dir() or not xml_path.exists():
            continue
        locale = android_locale(directory.name, source_language)
        if locale is None:
            continue
        strings = parse_strings_xml(xml_path)
        if strings:
            result.append((locale, strings))
            print(f"loaded {len(strings)} strings from {xml_path} ({locale})")
    return result


def sync(
    xcstrings_path: Path,
    res_dir: Path,
    *,
    dry_run: bool = False,
    delete_missing: bool = False,
) -> None:
    with xcstrings_path.open(encoding="utf-8") as file:
        catalog = json.load(file)

    source_language = catalog.get("sourceLanguage", "en")
    catalog_strings = catalog.setdefault("strings", {})
    translations: dict[str, dict[str, str]] = {}

    for locale, strings in discover_android_strings(res_dir, source_language):
        for key, value in strings.items():
            translations.setdefault(key, {})[locale] = value

    if not translations:
        raise ValueError(f"no Android strings found in {res_dir}")

    added = updated = deleted = 0
    for key, localized_values in translations.items():
        entry = catalog_strings.setdefault(
            key, {"extractionState": "manual", "localizations": {}}
        )
        localizations = entry.setdefault("localizations", {})
        for locale, value in localized_values.items():
            string_unit = localizations.get(locale, {}).get("stringUnit")
            if string_unit is None:
                localizations[locale] = {
                    "stringUnit": {"state": "translated", "value": value}
                }
                added += 1
            elif string_unit.get("value") != value:
                string_unit.update(state="translated", value=value)
                updated += 1

    if delete_missing:
        missing_keys = set(catalog_strings) - set(translations)
        for key in missing_keys:
            del catalog_strings[key]
        deleted = len(missing_keys)

    catalog["strings"] = dict(sorted(catalog_strings.items()))
    print(
        f"synced {len(translations)} keys: "
        f"{added} translations added, {updated} updated, {deleted} keys deleted"
    )

    if dry_run:
        print("dry run: no file written")
        return

    with xcstrings_path.open("w", encoding="utf-8") as file:
        json.dump(catalog, file, ensure_ascii=False, indent=2)
        file.write("\n")


def main() -> int:
    parser = argparse.ArgumentParser(
        description="Synchronize Android strings.xml files into Localizable.xcstrings."
    )
    parser.add_argument("--xcstrings", type=Path, required=True)
    parser.add_argument("--android-res", type=Path, required=True)
    parser.add_argument("--dry-run", action="store_true")
    parser.add_argument("--delete-missing", action="store_true")
    args = parser.parse_args()

    if not args.xcstrings.is_file():
        parser.error(f"catalog not found: {args.xcstrings}")
    if not args.android_res.is_dir():
        parser.error(f"Android resources directory not found: {args.android_res}")

    try:
        sync(
            args.xcstrings,
            args.android_res,
            dry_run=args.dry_run,
            delete_missing=args.delete_missing,
        )
    except (OSError, ValueError, json.JSONDecodeError) as error:
        print(f"error: {error}", file=sys.stderr)
        return 1
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
