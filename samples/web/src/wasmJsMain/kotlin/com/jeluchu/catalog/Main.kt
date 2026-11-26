package com.jeluchu.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
    val p = if (dark) DarkSage else LightSage
    CompositionLocalProvider(LocalPalette provides p) {
        MaterialTheme(colorScheme = p.scheme) {
            Surface(Modifier.fillMaxSize(), color = p.bg) {
                Column(Modifier.fillMaxSize()) {
                    Header(dark = dark, onToggle = { dark = !dark })
                    Column(
                        Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()).padding(horizontal = 40.dp, vertical = 32.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Explore, tweak, copy.", color = p.fg, fontSize = 40.sp, fontWeight = FontWeight.ExtraBold)
                            Text(
                                "Components, extensions and date helpers running live, with the code to use each one.",
                                color = p.muted, fontSize = 16.sp, modifier = Modifier.widthIn(max = 640.dp)
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Components", "Extensions", "Dates").forEach { name ->
                                Pill(name, selected = tab == name, onClick = { tab = name }, height = 44.dp)
                            }
                        }
                        when (tab) {
                            "Components" -> ComponentsTab()
                            "Extensions" -> ExtensionsTab()
                            else -> DatesTab()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Header(dark: Boolean, onToggle: () -> Unit) {
    val p = LocalPalette.current
    Row(
        Modifier.fillMaxWidth().background(p.panel).padding(horizontal = 32.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(Modifier.size(36.dp).clip(CircleShape).background(p.accent).border(3.dp, p.mint, CircleShape), contentAlignment = Alignment.Center) {
            Text("J", color = p.onAccent, fontWeight = FontWeight.ExtraBold)
        }
        Text("JchuComponents", color = p.fg, fontSize = 19.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f))
        Pill(if (dark) "Light" else "Dark", selected = false, onClick = onToggle, height = 44.dp)
    }
}

@Composable
private fun ComponentsTab() {
    val p = LocalPalette.current
    val families = remember { demos.map { it.family }.distinct() }
    var family by remember { mutableStateOf(families.first()) }
    var selected by remember { mutableStateOf(demos.first { it.family == families.first() }) }
    val items = demos.filter { it.family == family }

    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        families.forEach { f ->
            Pill(f, selected = f == family, onClick = { family = f; selected = demos.first { it.family == f } })
        }
    }
    FlowRow(horizontalArrangement = Arrangement.spacedBy(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        items.forEach { d ->
            val on = d == selected || (selected.family != family && d == items.first())
            Column(
                Modifier.widthIn(min = 300.dp, max = 360.dp).clip(RoundedCornerShape(20.dp)).background(p.panel)
                    .border(2.dp, if (on) p.accent else p.line, RoundedCornerShape(20.dp)).clickable { selected = d }
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
        CodeBlock(current.code)
    }
}
