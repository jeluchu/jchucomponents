package com.jeluchu.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeViewport
import com.jeluchu.jchucomponents.foundation.extensions.time.format
import com.jeluchu.jchucomponents.foundation.extensions.time.minusDays
import com.jeluchu.jchucomponents.foundation.extensions.time.minusMonths
import com.jeluchu.jchucomponents.foundation.extensions.time.plusDays
import com.jeluchu.jchucomponents.foundation.extensions.time.plusMonths
import com.jeluchu.jchucomponents.foundation.extensions.time.startOfMonth
import com.jeluchu.jchucomponents.ui.composables.chips.SelectableChip
import com.jeluchu.jchucomponents.ui.composables.loaders.DotsLoading
import com.jeluchu.jchucomponents.ui.composables.loaders.PulseLoading
import kotlinx.datetime.LocalDate

private val Sage = Color(0xFFB9C78F)
private val Mint = Color(0xFF8FC4A0)
private val Slate = Color(0xFF1B2128)
private val Panel = Color(0xFF232B33)

private val SageScheme =
    darkColorScheme(
        primary = Sage,
        onPrimary = Slate,
        background = Slate,
        surface = Panel,
        onSurface = Color(0xFFEEF0E4),
        onBackground = Color(0xFFEEF0E4)
    )

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport { MaterialTheme(colorScheme = SageScheme) { CatalogApp() } }
}

@Composable
private fun CatalogApp() {
    var tab by remember { mutableStateOf("Components") }
    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(Modifier.verticalScroll(rememberScrollState()).padding(32.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Text("JchuComponents", style = MaterialTheme.typography.headlineLarge)
            Text("Explore, tweak, copy.", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Components", "Dates").forEach { name ->
                    Button(
                        onClick = { tab = name },
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = if (tab == name) Sage else Panel,
                                contentColor = if (tab == name) Slate else Color.White
                            )
                    ) { Text(name) }
                }
            }
            if (tab == "Components") ComponentsTab() else DatesTab()
        }
    }
}

@Composable
private fun Card(title: String, content: @Composable () -> Unit) {
    Column(
        Modifier.clip(RoundedCornerShape(20.dp)).background(Panel).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, color = Mint)
        content()
    }
}

@Composable
private fun ComponentsTab() {
    var selected by remember { mutableStateOf(setOf("Action")) }
    Card("SelectableChip") {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Action", "Drama", "Sci-Fi").forEach { genre ->
                SelectableChip(
                    label = genre,
                    selected = genre in selected,
                    color = Sage,
                    textColor = Slate,
                    onClick = { on -> selected = if (on) selected + genre else selected - genre }
                )
            }
        }
    }
    Card("DotsLoading") {
        Box(Modifier.height(70.dp)) { DotsLoading(loaderColor = Sage) }
    }
    Card("PulseLoading") {
        Box(Modifier.size(150.dp)) { PulseLoading(maxPulseSize = 150f, pulseColor = Mint.copy(alpha = 0.4f), centreColor = Sage) }
    }
}

@Composable
private fun DatesTab() {
    var date by remember { mutableStateOf(LocalDate(2026, 11, 26)) }
    Card("LocalDate helpers") {
        Text(date.toString(), style = MaterialTheme.typography.headlineMedium)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { date = date.minusMonths(1) }) { Text("minusMonths(1)") }
            Button(onClick = { date = date.minusDays(1) }) { Text("minusDays(1)") }
            Button(onClick = { date = date.plusDays(1) }) { Text("plusDays(1)") }
            Button(onClick = { date = date.plusMonths(1) }) { Text("plusMonths(1)") }
        }
        listOf("dd/MM/yyyy", "yyyy-MM-dd", "d MMM yyyy", "EEEE, d MMMM").forEach { pattern ->
            Text("format(\"$pattern\") -> ${date.format(pattern)}")
        }
        Text("startOfMonth() -> ${date.startOfMonth()}")
    }
}
