package com.jeluchu.jchucomponents.foundation.extensions.lists

import com.jeluchu.jchucomponents.foundation.text.removeDiacritics

/**
 * Returns the items whose [selector] text contains [query], ignoring case and accents.
 * A blank [query] matches every item.
 */
fun <T> List<T>.search(query: String, selector: (T) -> String): List<T> {
    val normalizedQuery = query.normalizeForSearch()
    return filter { selector(it).normalizeForSearch().contains(normalizedQuery) }
}

private fun String.normalizeForSearch(): String = lowercase().removeDiacritics()
