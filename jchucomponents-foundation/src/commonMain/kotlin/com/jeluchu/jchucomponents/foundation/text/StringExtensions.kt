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
