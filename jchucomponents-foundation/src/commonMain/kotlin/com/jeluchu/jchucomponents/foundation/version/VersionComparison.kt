package com.jeluchu.jchucomponents.foundation.version

/**
 * Compares two dot-separated versions.
 *
 * Supports an optional `v` prefix, missing trailing components, semantic-version
 * prerelease identifiers and build metadata. Returns a negative value when [first]
 * is older, zero when both versions have the same precedence, and a positive value
 * when [first] is newer.
 */
@Throws(IllegalArgumentException::class)
fun compareVersions(
    first: String,
    second: String
): Int = ParsedVersion.parse(first).compareTo(ParsedVersion.parse(second))

@Throws(IllegalArgumentException::class)
fun String.compareVersionTo(other: String): Int = compareVersions(this, other)

private data class ParsedVersion(
    val core: List<Long>,
    val prerelease: List<String>?
) : Comparable<ParsedVersion> {
    override fun compareTo(other: ParsedVersion): Int {
        val coreSize = maxOf(core.size, other.core.size)
        repeat(coreSize) { index ->
            val comparison =
                core
                    .getOrElse(index) { 0L }
                    .compareTo(other.core.getOrElse(index) { 0L })
            if (comparison != 0) return comparison
        }

        val firstPrerelease = prerelease
        val secondPrerelease = other.prerelease
        if (firstPrerelease == null && secondPrerelease == null) return 0
        if (firstPrerelease == null) return 1
        if (secondPrerelease == null) return -1

        val prereleaseSize = minOf(firstPrerelease.size, secondPrerelease.size)
        repeat(prereleaseSize) { index ->
            val comparison =
                compareIdentifiers(
                    first = firstPrerelease[index],
                    second = secondPrerelease[index]
                )
            if (comparison != 0) return comparison
        }
        return firstPrerelease.size.compareTo(secondPrerelease.size)
    }

    companion object {
        fun parse(value: String): ParsedVersion {
            val normalized = value.trim().removePrefix("v").removePrefix("V")
            require(normalized.isNotEmpty()) { "Version cannot be empty" }

            val withoutMetadata = normalized.substringBefore("+")
            val coreValue = withoutMetadata.substringBefore("-")
            val prereleaseValue = withoutMetadata.substringAfter("-", missingDelimiterValue = "")

            val core =
                coreValue.split(".").map { component ->
                    require(component.isNotEmpty() && component.all(Char::isDigit)) {
                        "Invalid version: $value"
                    }
                    component.toLongOrNull() ?: throw IllegalArgumentException(
                        "Version component is too large: $component"
                    )
                }
            val prerelease =
                prereleaseValue.takeIf(String::isNotEmpty)?.split(".")?.also {
                    require(
                        it.all { identifier ->
                            identifier.isNotEmpty() &&
                                identifier.all { character -> character.isLetterOrDigit() || character == '-' }
                        }
                    ) {
                        "Invalid version: $value"
                    }
                }

            return ParsedVersion(core = core, prerelease = prerelease)
        }
    }
}

private fun compareIdentifiers(
    first: String,
    second: String
): Int {
    val firstNumber = first.toLongOrNull()
    val secondNumber = second.toLongOrNull()
    return when {
        firstNumber != null && secondNumber != null -> firstNumber.compareTo(secondNumber)
        firstNumber != null -> -1
        secondNumber != null -> 1
        else -> first.compareTo(second)
    }
}
