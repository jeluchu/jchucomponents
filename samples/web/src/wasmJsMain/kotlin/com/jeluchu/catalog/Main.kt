package com.jeluchu.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.ComposeViewport

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport { CatalogApp() }
}

@Composable
private fun CatalogApp() {
    var dark by remember { mutableStateOf(true) }
    var tab by remember { mutableStateOf("Components") }
    var family by remember { mutableStateOf(demos.first().family) }
    var selected by remember { mutableStateOf(demos.first()) }
    var query by remember { mutableStateOf("") }
    val results = remember(query) { searchCatalog(query) }
    val p = if (dark) DarkSage else LightSage
    BoxWithConstraints(Modifier.fillMaxSize()) {
    val compact = maxWidth < 700.dp
    CompositionLocalProvider(LocalPalette provides p, LocalCompact provides compact) {
        MaterialTheme(colorScheme = p.scheme) {
            Surface(Modifier.fillMaxSize(), color = p.bg) {
                Column(Modifier.fillMaxSize()) {
                    Header(dark = dark, onToggle = { dark = !dark }, query = query, onQueryChange = { query = it })
                    if (query.isNotBlank()) {
                        SearchResults(
                            results, query, modifier = Modifier.padding(horizontal = if (compact) 12.dp else 32.dp, vertical = 8.dp),
                            onPick = { e ->
                                tab = e.tab
                                e.demo?.let { family = it.family; selected = it }
                                query = ""
                            }
                        )
                    }
                    Column(
                        Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()).padding(horizontal = if (compact) 16.dp else 40.dp, vertical = if (compact) 20.dp else 32.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Explore, tweak, copy.", color = p.fg, fontSize = if (compact) 30.sp else 40.sp, fontWeight = FontWeight.ExtraBold)
                            Text(
                                "Components, extensions and date helpers running live, with the code to use each one.",
                                color = p.muted, fontSize = 16.sp, modifier = Modifier.widthIn(max = 640.dp)
                            )
                        }
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Components", "Extensions", "Dates").forEach { name ->
                                Pill(name, selected = tab == name, onClick = { tab = name }, height = 44.dp)
                            }
                        }
                        when (tab) {
                            "Components" -> ComponentsTab(family, { family = it }, selected, { selected = it })
                            "Extensions" -> ExtensionsTab()
                            else -> DatesTab()
                        }
                    }
                }
            }
        }
    }
    }
}

@Composable
private fun Header(dark: Boolean, onToggle: () -> Unit, query: String, onQueryChange: (String) -> Unit) {
    val p = LocalPalette.current
    Row(
        Modifier.fillMaxWidth().background(p.panel).padding(horizontal = if (LocalCompact.current) 14.dp else 32.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(Modifier.size(36.dp).clip(CircleShape).background(p.accent).border(3.dp, p.mint, CircleShape), contentAlignment = Alignment.Center) {
            Text("J", color = p.onAccent, fontWeight = FontWeight.ExtraBold)
        }
        if (!LocalCompact.current) Text("JchuComponents", color = p.fg, fontSize = 19.sp, fontWeight = FontWeight.ExtraBold)
        SearchBar(query, onQueryChange, Modifier.weight(1f).widthIn(max = 520.dp))
        if (!LocalCompact.current) Box(Modifier.weight(0.001f))
        Pill(if (dark) "Light" else "Dark", selected = false, onClick = onToggle, height = 44.dp)
    }
}

@Composable
private fun ComponentsTab(family: String, onFamily: (String) -> Unit, selected: Demo, onSelect: (Demo) -> Unit) {
    val p = LocalPalette.current
    val families = remember { demos.map { it.family }.distinct() }
    val items = demos.filter { it.family == family }

    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        families.forEach { f ->
            Pill(f, selected = f == family, onClick = { onFamily(f); onSelect(demos.first { it.family == f }) })
        }
    }
    FlowRow(horizontalArrangement = Arrangement.spacedBy(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        items.forEach { d ->
            val on = d == selected || (selected.family != family && d == items.first())
            Column(
                Modifier.widthIn(max = 360.dp).fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(p.panel)
                    .border(2.dp, if (on) p.accent else p.line, RoundedCornerShape(20.dp)).clickable { onSelect(d) }
            ) {
                Box(Modifier.fillMaxWidth().heightIn(min = 150.dp).background(p.stage).padding(20.dp), contentAlignment = Alignment.Center) { d.preview() }
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(d.name, color = p.fg, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(d.tag, color = p.softFg, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(p.soft).padding(horizontal = 8.dp, vertical = 2.dp))
                    }
                    Text(d.description, color = p.muted, fontSize = 13.sp, lineHeight = 19.sp)
                }
            }
        }
    }
    val current = if (selected.family == family) selected else items.first()
    Section(current.name, "How to use it", Modifier.fillMaxWidth()) {
        Text(current.description, color = p.muted, fontSize = 15.sp)
        val uri = LocalUriHandler.current
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Pill("View source", true, { uri.openUri(current.sourceUrl()) }, height = 44.dp)
            Pill("API reference", false, { uri.openUri("../") }, height = 44.dp)
        }
        CodeBlock(current.code)
    }
}
