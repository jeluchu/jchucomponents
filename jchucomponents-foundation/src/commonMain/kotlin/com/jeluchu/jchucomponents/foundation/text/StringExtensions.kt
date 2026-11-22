package com.jeluchu.jchucomponents.foundation.text

/**
 * Splits this string into equally sized groups and joins them with [separator].
 *
 * The final group may contain fewer than [groupSize] characters.
 */
fun String.formatInGroups(
    groupSize: Int = 4,
    separator: String = "-"
): String {
    require(groupSize > 0) { "Group size must be greater than zero" }
    return chunked(groupSize).joinToString(separator)
}

/** Returns this string, or [default] when it is empty. */
fun String.getTranslateOrDefault(default: String): String = ifEmpty { default }

/** `true` when this string is not the literal `"0"` (a number-as-string flag used by several backends). */
fun String.isNotZero(): Boolean = this != "0"

/**
 * `true` when a store/availability code means "not available": empty, `"0"`, `"na"`/`"NA"`, `"nts"` or `"NFS"`.
 */
fun String.isBuyNotAvailability(): Boolean =
    isEmpty() || this == "0" || this == "na" || this == "NA" || this == "nts" || this == "NFS"
