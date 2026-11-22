package com.jeluchu.jchucomponents.foundation.text

import com.jeluchu.jchucomponents.foundation.extensions.empty

private val youTubeIdRegex =
    Regex(
        "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch\\?v=)([^#&?]*).*$",
        RegexOption.IGNORE_CASE
    )

private val ipv4Regex =
    Regex("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")

/** The last path segment of this URL, without query or fragment. */
fun String.getLastBitFromUrl(): String = replaceFirst(".*/([^/?]+).*".toRegex(), "$1")

/** The YouTube video id of a `youtu.be`, `embed`, `v/` or `watch\\?v=` URL, or `null` when it is not one. */
fun String.extractYTId(): String? = youTubeIdRegex.matchEntire(this)?.groupValues?.getOrNull(1)

/** The part of a decimal number text before the dot: `"12.50"` becomes `"12"`. */
fun String.deleteDouble(): String = split(".")[0]

/** Returns this text, or [default] when it is `null`. */
fun String?.safeValue(default: String = String.empty()): String = this ?: default

/** Removes line breaks and carriage returns; `null` becomes an empty text. */
fun String?.removeCharactersWeirds(): String =
    this?.replace("\n", String.empty())?.replace("\r", String.empty()) ?: String.empty()

/** `true` when this text is empty or the literal `"null"` (case insensitive). */
fun CharSequence.isEmptyString(): Boolean = isEmpty() || toString().equals("null", ignoreCase = true)

/** `true` when this text contains an IPv4 address. */
val String.isIp: Boolean get() = ipv4Regex.containsMatchIn(this)

/** The symbol of an ISO currency code; unknown codes fall back to `€`. */
fun String.getCurrencyString(): String =
    when (this) {
        "EUR" -> "€"
        "GBP" -> "£"
        "USD" -> "$"
        "CHF" -> "Fr"
        "SEK", "DKK" -> "kr"
        "RUB" -> "₽"
        else -> "€"
    }

/** Lowercases this text and strips its diacritics, so searches ignore case and accents. */
fun String.normalizeForSearch(): String = lowercase().removeDiacritics()
