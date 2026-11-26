package com.jeluchu.jchucomponents.foundation.time.providers

import kotlin.time.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

/**
 * Pure Kotlin implementation (no java.text on wasm). Supports the common SimpleDateFormat tokens:
 * `yyyy yy MMMM MMM MM M dd d HH H mm ss SSS EEEE EEE` and `'quoted'` literals. Names are in English.
 */
actual object DateProvider {
    private val months = Month.entries.map { it.name.lowercase().replaceFirstChar(Char::uppercase) }
    private val days = DayOfWeek.entries.map { it.name.lowercase().replaceFirstChar(Char::uppercase) }

    actual fun formatLocalDate(date: LocalDate, pattern: String): String =
        format(pattern, date.year, date.month.number, date.day, 0, 0, 0, 0, date.dayOfWeek)

    actual fun formatLocalDateTime(dateTime: LocalDateTime, pattern: String): String =
        format(
            pattern, dateTime.year, dateTime.month.number, dateTime.day, dateTime.hour, dateTime.minute,
            dateTime.second, dateTime.nanosecond / 1_000_000, dateTime.date.dayOfWeek
        )

    actual fun parseLocalDate(value: String, pattern: String): LocalDate {
        val f = parse(value, pattern)
        return LocalDate(f.getValue("y"), f.getValue("M"), f.getValue("d"))
    }

    actual fun parseLocalDateTime(value: String, pattern: String): LocalDateTime {
        val f = parse(value, pattern)
        return LocalDateTime(
            f.getValue("y"), f.getValue("M"), f.getValue("d"),
            f["H"] ?: 0, f["m"] ?: 0, f["s"] ?: 0, (f["S"] ?: 0) * 1_000_000
        )
    }

    actual fun firstDayOfWeekFromLocale(): DayOfWeek = DayOfWeek.MONDAY

    actual fun currentDayOfWeek(): DayOfWeek =
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.dayOfWeek

    private fun tokens(pattern: String): List<Pair<Char, String>> {
        val out = mutableListOf<Pair<Char, String>>()
        var i = 0
        while (i < pattern.length) {
            val c = pattern[i]
            if (c == '\'') {
                val end = pattern.indexOf('\'', i + 1).let { if (it < 0) pattern.length else it }
                out += '\'' to pattern.substring(i + 1, end)
                i = end + 1
            } else if (c.isLetter()) {
                var j = i
                while (j < pattern.length && pattern[j] == c) j++
                out += c to pattern.substring(i, j)
                i = j
            } else {
                out += '\u0000' to c.toString()
                i++
            }
        }
        return out
    }

    private fun format(
        pattern: String, y: Int, m: Int, d: Int, h: Int, min: Int, s: Int, ms: Int, dow: DayOfWeek
    ): String = buildString {
        tokens(pattern).forEach { (c, t) ->
            when (c) {
                'y' -> append(if (t.length == 2) (y % 100).toString().padStart(2, '0') else y.toString().padStart(t.length, '0'))
                'M' -> append(
                    when {
                        t.length >= 4 -> months[m - 1]
                        t.length == 3 -> months[m - 1].take(3)
                        else -> m.toString().padStart(t.length, '0')
                    }
                )
                'd' -> append(d.toString().padStart(t.length, '0'))
                'H' -> append(h.toString().padStart(t.length, '0'))
                'm' -> append(min.toString().padStart(t.length, '0'))
                's' -> append(s.toString().padStart(t.length, '0'))
                'S' -> append(ms.toString().padStart(3, '0').take(t.length))
                'E' -> append(if (t.length >= 4) days[dow.ordinal] else days[dow.ordinal].take(3))
                else -> append(t)
            }
        }
    }

    private fun parse(value: String, pattern: String): Map<String, Int> {
        val result = mutableMapOf<String, Int>()
        var pos = 0
        tokens(pattern).forEach { (c, t) ->
            fun digits(max: Int): Int {
                val start = pos
                while (pos < value.length && pos - start < max && value[pos].isDigit()) pos++
                require(pos > start) { "Cannot parse date: $value" }
                return value.substring(start, pos).toInt()
            }
            when (c) {
                'y' -> result["y"] = digits(if (t.length == 2) 2 else 4).let { if (t.length == 2) 2000 + it else it }
                'M' -> if (t.length >= 3) {
                    val idx = months.indexOfFirst { value.startsWith(if (t.length == 3) it.take(3) else it, pos, ignoreCase = true) }
                    require(idx >= 0) { "Cannot parse date: $value" }
                    pos += if (t.length == 3) 3 else months[idx].length
                    result["M"] = idx + 1
                } else result["M"] = digits(2)
                'd' -> result["d"] = digits(2)
                'H' -> result["H"] = digits(2)
                'm' -> result["m"] = digits(2)
                's' -> result["s"] = digits(2)
                'S' -> result["S"] = digits(3)
                else -> {
                    require(value.startsWith(t, pos)) { "Cannot parse date: $value" }
                    pos += t.length
                }
            }
        }
        return result
    }
}
