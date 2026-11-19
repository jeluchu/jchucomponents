package com.jeluchu.jchucomponents.monitoring

/** Firebase Analytics naming limits, applied once here so every app sends valid events. */
object JchuAnalyticsRules {
    const val MAX_NAME_LENGTH = 40
    const val MAX_PARAM_VALUE = 100
    const val MAX_PROPERTY_VALUE = 36
    const val MAX_PARAMS = 25

    /** Letters, digits and `_`, starting with a letter, at most [MAX_NAME_LENGTH] characters. */
    fun eventName(raw: String): String = identifier(raw, fallback = "event")

    fun propertyName(raw: String): String = identifier(raw, fallback = "property").take(24)

    fun paramName(raw: String): String = identifier(raw, fallback = "param")

    /** Sanitized names, bounded values and at most [MAX_PARAMS] entries. */
    fun params(raw: Map<String, Any?>): Map<String, Any?> =
        raw.entries.take(MAX_PARAMS).associate { (key, value) ->
            paramName(key) to when (value) {
                null -> ""
                is Int, is Long, is Float, is Double, is Boolean -> value
                is String -> value.take(MAX_PARAM_VALUE)
                else -> value.toString().take(MAX_PARAM_VALUE)
            }
        }

    private fun identifier(raw: String, fallback: String): String {
        val cleaned = raw.trim().map { if (it.isLetterOrDigit() && it.code < 128 || it == '_') it else '_' }.joinToString("")
        val startsWithLetter = cleaned.dropWhile { it == '_' || it.isDigit() }
        val result = if (startsWithLetter.isEmpty()) fallback else startsWithLetter
        return result.take(MAX_NAME_LENGTH)
    }
}
