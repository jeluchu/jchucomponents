package com.jeluchu.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeluchu.jchucomponents.foundation.text.removeDiacritics

/** Where a search result leads: a tab and, for components, the demo to open. */
class SearchEntry(
    val title: String,
    val subtitle: String,
    val kind: String,
    val tab: String,
    val demo: Demo? = null
)

private val extensionEntries =
    listOf(
        "Color.opacity()" to "Dims a color when it is disabled",
        "Color.isDark()" to "True when the luminance is below 0.5",
        "Int.milliSecondsToTimer()" to "Formats milliseconds as m:ss or h:mm:ss",
        "Int.thousandsFormat()" to "Groups digits by thousands: 1.234.567",
        "Modifier.shake()" to "Wiggles an element, ideal for errors",
        "Modifier.bounceClick()" to "Scale-down feedback on press",
        "Modifier.conditional()" to "Applies a modifier only when a condition holds"
    ).map { (title, description) -> SearchEntry(title, description, "Extension", "Extensions") }

private val dateEntries =
    listOf(
        "LocalDate.format()" to "Formats a date with a SimpleDateFormat-style pattern",
        "LocalDate.plusDays() / minusDays()" to "Moves a date by days",
        "LocalDate.plusMonths() / minusMonths()" to "Moves a date by months",
        "LocalDate.startOfMonth()" to "First day of the month",
        "LocalDate.isSameDay()" to "Compares two dates by day",
        "LocalDate.isAfter()" to "True when a date is later than another",
        "String.toLocalDate()" to "Parses text with a pattern"
    ).map { (title, description) -> SearchEntry(title, description, "Date", "Dates") }

private fun String.norm() = removeDiacritics().lowercase()

fun searchCatalog(query: String): List<SearchEntry> {
    val terms = query.norm().split(' ').filter { it.isNotBlank() }
    if (terms.isEmpty()) return emptyList()
    val all = demos.map { SearchEntry(it.name, it.description, it.family, "Components", it) } + extensionEntries + dateEntries
    return all
        .filter { e -> val hay = "${e.title} ${e.subtitle} ${e.kind}".norm(); terms.all { it in hay } }
        .sortedBy { if (it.title.norm().startsWith(terms.first())) 0 else 1 }
        .take(8)
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit, modifier: Modifier = Modifier) {
    val p = LocalPalette.current
    Row(
        modifier.height(44.dp).clip(RoundedCornerShape(12.dp)).background(p.bg).border(1.dp, p.line, RoundedCornerShape(12.dp)).padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(Icons.Default.Search, contentDescription = "Search", tint = p.muted)
        Box(Modifier.weight(1f)) {
            if (query.isEmpty()) Text(if (LocalCompact.current) "Search…" else "Search components, extensions, dates…", color = p.muted, fontSize = 14.sp, maxLines = 1)
            BasicTextField(
                value = query, onValueChange = onQueryChange, singleLine = true,
                textStyle = TextStyle(color = p.fg, fontSize = 14.sp), cursorBrush = SolidColor(p.accent),
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (query.isNotEmpty()) {
            Icon(Icons.Default.Clear, contentDescription = "Clear search", tint = p.muted, modifier = Modifier.clickable { onQueryChange("") })
        }
    }
}

@Composable
fun SearchResults(results: List<SearchEntry>, query: String, onPick: (SearchEntry) -> Unit, modifier: Modifier = Modifier) {
    val p = LocalPalette.current
    Column(
        modifier.widthIn(max = 560.dp).clip(RoundedCornerShape(16.dp)).background(p.panel).border(1.dp, p.line, RoundedCornerShape(16.dp)).padding(8.dp)
    ) {
        if (results.isEmpty()) {
            Text("No results for \"$query\"", color = p.muted, fontSize = 14.sp, modifier = Modifier.padding(12.dp))
        }
        results.forEach { e ->
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).clickable { onPick(e) }.padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(Modifier.weight(1f)) {
                    Text(e.title, color = p.fg, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(e.subtitle, color = p.muted, fontSize = 12.sp, maxLines = 1)
                }
                Text(
                    e.kind, color = p.softFg, fontSize = 11.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(p.soft).padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }
    }
}
