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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
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
    val demo: Demo? = null,
    val util: UtilCard? = null
)

private val extensionEntries =
    listOf(
        "Color.opacity()" to "Dims a color when it is disabled",
        "Color.isDark()" to "True when the luminance is below 0.5",
        "Int.milliSecondsToTimer()" to "Formats milliseconds as m:ss or h:mm:ss",
        "Int.thousandsFormat()" to "Groups digits by thousands: 1.234.567",
        "Modifier.shake()" to "Wiggles an element, ideal for errors",
        "Modifier.bounceClick()" to "Scale-down feedback on press",
        "Modifier.conditional()" to "Applies a modifier only when a condition holds",
        "Modifier.dashedBorder()" to "Draws a dashed border around any element",
        "Modifier.noRippleClickable()" to "Clickable without a ripple",
        "Modifier.click()" to "Simple click helper",
        "Modifier.whenNotNull()" to "Applies a modifier only when a value is not null",
        "LazyListScope.itemIf()" to "Adds a lazy item only when a condition holds",
        "Int.cornerRadius() and Int.Height()" to "Shape and spacer shortcuts on Int",
        "Color?.orTransparent()" to "Default color for a nullable color",
        "Color.random()" to "Random color"
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

/** Spanish search terms mapped to the English words used in the catalog. */
private val synonyms: Map<String, List<String>> =
    mapOf(
        "fecha" to listOf("date", "localdate"), "fechas" to listOf("date", "localdate"), "mes" to listOf("month"),
        "meses" to listOf("month"), "dia" to listOf("day"), "dias" to listOf("day"), "ano" to listOf("year"),
        "carga" to listOf("loading", "loader", "progress"), "cargando" to listOf("loading", "loader"),
        "boton" to listOf("button"), "tarjeta" to listOf("card"), "buscar" to listOf("search"), "busqueda" to listOf("search"),
        "buscador" to listOf("search"), "texto" to listOf("text"), "progreso" to listOf("progress"),
        "ajustes" to listOf("preference", "setting"), "preferencias" to listOf("preference"), "opciones" to listOf("preference", "choice"),
        "pestana" to listOf("tab"), "pestanas" to listOf("tab"), "hoja" to listOf("sheet"), "aviso" to listOf("snackbar"),
        "mensaje" to listOf("snackbar"), "ayuda" to listOf("tooltip"), "numero" to listOf("number", "int"), "numeros" to listOf("number", "int"),
        "tiempo" to listOf("timer", "time"), "temporizador" to listOf("timer"), "formato" to listOf("format"),
        "contador" to listOf("counter", "count"), "divisor" to listOf("divider"), "separador" to listOf("divider"),
        "campo" to listOf("field"), "entrada" to listOf("field", "input"), "color" to listOf("color"), "colores" to listOf("color"),
        "animacion" to listOf("animated", "shake", "bounce"), "temblar" to listOf("shake"), "rebote" to listOf("bounce"),
        "etiqueta" to listOf("tag", "chip"), "interruptor" to listOf("switch"), "paginas" to listOf("page", "pager"),
        "lista" to listOf("list"), "validar" to listOf("validator", "validation"), "tarjeta credito" to listOf("credit"), "cifrado" to listOf("cipher", "crypto"), "version" to listOf("version"), "distancia" to listOf("distance"), "aleatorio" to listOf("random"), "mayusculas" to listOf("capitalize"), "acentos" to listOf("diacritics"), "dinero" to listOf("currency", "iban"), "banco" to listOf("iban"), "miles" to listOf("thousands")
    )

fun searchCatalog(query: String): List<SearchEntry> {
    val terms = query.norm().split(' ').filter { it.isNotBlank() }
    if (terms.isEmpty()) return emptyList()
    val all = demos.map { SearchEntry(it.name, it.description, it.family, "Components", it) } +
        utilities.map { SearchEntry(it.name, it.description, it.group, "Utilities", util = it) } + extensionEntries + dateEntries
    return all
        .filter { e -> val hay = "${e.title} ${e.subtitle} ${e.kind}".norm(); terms.all { t -> t in hay || synonyms[t].orEmpty().any { it in hay } } }
        .sortedBy { if (it.title.norm().startsWith(terms.first())) 0 else 1 }
        .take(8)
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    focusRequester: FocusRequester,
    onKey: (Key) -> Boolean,
    modifier: Modifier = Modifier
) {
    val p = LocalPalette.current
    val focusManager = LocalFocusManager.current
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
                modifier = Modifier.fillMaxWidth().focusRequester(focusRequester).onPreviewKeyEvent { e ->
                    if (e.type != KeyEventType.KeyDown) {
                        false
                    } else {
                        if (e.key == Key.Escape) focusManager.clearFocus()
                        onKey(e.key)
                    }
                }
            )
        }
        if (query.isNotEmpty()) {
            Icon(Icons.Default.Clear, contentDescription = "Clear search", tint = p.muted, modifier = Modifier.clickable { onQueryChange("") })
        }
    }
}

@Composable
fun SearchResults(results: List<SearchEntry>, query: String, activeIndex: Int, onPick: (SearchEntry) -> Unit, modifier: Modifier = Modifier) {
    val p = LocalPalette.current
    Column(
        modifier.widthIn(max = 560.dp).clip(RoundedCornerShape(16.dp)).background(p.panel).border(1.dp, p.line, RoundedCornerShape(16.dp)).padding(8.dp)
    ) {
        if (results.isEmpty()) {
            Text("No results for \"$query\"", color = p.muted, fontSize = 14.sp, modifier = Modifier.padding(12.dp))
        }
        results.forEachIndexed { i, e ->
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(if (i == activeIndex) p.stage else androidx.compose.ui.graphics.Color.Transparent).clickable { onPick(e) }.padding(horizontal = 12.dp, vertical = 10.dp),
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
