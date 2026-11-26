package com.jeluchu.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeluchu.jchucomponents.foundation.extensions.time.format
import com.jeluchu.jchucomponents.foundation.extensions.time.isAfter
import com.jeluchu.jchucomponents.foundation.extensions.time.isSameDay
import com.jeluchu.jchucomponents.foundation.extensions.time.minusDays
import com.jeluchu.jchucomponents.foundation.extensions.time.minusMonths
import com.jeluchu.jchucomponents.foundation.extensions.time.minusYears
import com.jeluchu.jchucomponents.foundation.extensions.time.plusYears
import com.jeluchu.jchucomponents.foundation.extensions.time.plusDays
import com.jeluchu.jchucomponents.foundation.extensions.time.plusMonths
import com.jeluchu.jchucomponents.foundation.extensions.time.startOfMonth
import kotlin.time.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun DatesTab() {
    val p = LocalPalette.current
    val today = remember { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date }
    var date by remember { mutableStateOf(today) }
    var pattern by remember { mutableStateOf("dd/MM/yyyy") }
    val formatted = date.format(pattern)

    FlowRow(horizontalArrangement = Arrangement.spacedBy(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Section("LocalDate helpers", "Playground", Modifier.widthIn(max = 440.dp).fillMaxWidth()) {
            Text(date.toString(), color = p.fg, fontSize = 34.sp, fontWeight = FontWeight.ExtraBold)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Pill("minusMonths(1)", false, { date = date.minusMonths(1) }, mono = true, height = 44.dp)
                Pill("minusDays(1)", false, { date = date.minusDays(1) }, mono = true, height = 44.dp)
                Pill("plusDays(1)", false, { date = date.plusDays(1) }, mono = true, height = 44.dp)
                Pill("plusMonths(1)", false, { date = date.plusMonths(1) }, mono = true, height = 44.dp)
                Pill("minusYears(1)", false, { date = date.minusYears(1) }, mono = true, height = 44.dp)
                Pill("plusYears(1)", false, { date = date.plusYears(1) }, mono = true, height = 44.dp)
                Pill("today", false, { date = today }, mono = true, height = 44.dp)
            }
            Text("FORMAT(PATTERN)", color = p.muted, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("dd/MM/yyyy", "yyyy-MM-dd", "d MMM yyyy", "EEEE, d MMMM").forEach { pt ->
                    Pill(pt, selected = pt == pattern, onClick = { pattern = pt }, mono = true)
                }
            }
        }
        Section("What you get back", "Result", Modifier.widthIn(max = 440.dp).fillMaxWidth()) {
            listOf(
                "format(\"$pattern\")" to formatted,
                "startOfMonth()" to date.startOfMonth().toString(),
                "plusYears(1)" to date.plusYears(1).toString(),
                "isSameDay(today)" to date.isSameDay(today).toString(),
                "isAfter(today)" to date.isAfter(today).toString()
            ).forEach { (fn, out) ->
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(p.stage).padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(fn, color = p.muted, fontFamily = FontFamily.Monospace, fontSize = 13.sp, modifier = Modifier.weight(1f, fill = false))
                    Text(out, color = p.fg, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                }
            }
            CodeBlock(
                """val date = LocalDate(${date.year}, ${date.month.ordinal + 1}, ${date.day})
date.format("$pattern")
// "$formatted"

"26/11/2026".toLocalDate("dd/MM/yyyy")"""
            )
        }
    }
}
