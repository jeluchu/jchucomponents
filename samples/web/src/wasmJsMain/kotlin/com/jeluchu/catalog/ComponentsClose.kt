@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class, androidx.compose.foundation.ExperimentalFoundationApi::class)

package com.jeluchu.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeluchu.jchucomponents.ui.composables.divider.DashedDivider
import com.jeluchu.jchucomponents.ui.composables.loaders.ProgressIndicator
import com.jeluchu.jchucomponents.ui.composables.structures.ScaffoldStates
import com.jeluchu.jchucomponents.ui.foundation.lists.LazyColumnForIndexed
import com.jeluchu.jchucomponents.ui.foundation.lists.LazyRowFor
import com.jeluchu.jchucomponents.ui.foundation.lists.LazyRowForIndexed
import com.jeluchu.jchucomponents.ui.foundation.lists.ListColumn
import com.jeluchu.jchucomponents.ui.foundation.lists.ListRow
import com.jeluchu.jchucomponents.ui.foundation.lists.composition.ComposePagerSnapHelper

private fun narrow2() = Modifier.widthIn(max = 300.dp).fillMaxWidth()

@Composable
private fun Chip2(label: String, color: Color, modifier: Modifier = Modifier) {
    val p = LocalPalette.current
    Box(modifier.clip(RoundedCornerShape(10.dp)).background(color).padding(horizontal = 14.dp, vertical = 10.dp), contentAlignment = Alignment.Center) {
        Text(label, color = p.onAccent, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}

val closeDemos: List<Demo> by lazy {
    listOf(
        Demo(
            "Layout", "LazyRowFor", "Compose", "LazyRow that takes an Array of items directly.",
            """LazyRowFor(items = arrayOf("One", "Two", "Three")) { item ->
    Text(item)
}"""
        ) {
            val p = LocalPalette.current
            LazyRowFor(items = arrayOf("One", "Two", "Three", "Four", "Five", "Six"), modifier = narrow2(), horizontalArrangement = Arrangement.spacedBy(6.dp)) { Chip2(it, p.accent) }
        },
        Demo(
            "Layout", "LazyRowForIndexed", "Compose", "LazyRow over an Array that also gives you each item's index.",
            """LazyRowForIndexed(items = arrayOf("A", "B", "C")) { index, item ->
    Text("${'$'}index: ${'$'}item")
}"""
        ) {
            val p = LocalPalette.current
            LazyRowForIndexed(items = arrayOf("A", "B", "C", "D", "E", "F"), modifier = narrow2(), horizontalArrangement = Arrangement.spacedBy(6.dp)) { index, item -> Chip2("$index: $item", if (index % 2 == 0) p.accent else p.mint) }
        },
        Demo(
            "Layout", "LazyColumnForIndexed", "Compose", "LazyColumn over a List or Array that also gives you each item's index.",
            """LazyColumnForIndexed(items = listOf("A", "B", "C")) { index, item ->
    Text("${'$'}index: ${'$'}item")
}"""
        ) {
            val p = LocalPalette.current
            Box(narrow2().height(120.dp)) {
                LazyColumnForIndexed(items = listOf("Alpha", "Beta", "Gamma", "Delta", "Epsilon")) { index, item -> Chip2("${index + 1}. $item", p.accent, Modifier.fillMaxWidth().padding(vertical = 3.dp)) }
            }
        },
        Demo(
            "Layout", "ListColumn", "Compose", "LazyColumn without the legacy overscroll effect, built with a LazyListScope.",
            """ListColumn {
    items(20) { Text("Item ${'$'}it") }
}"""
        ) {
            val p = LocalPalette.current
            Box(narrow2().height(120.dp)) {
                ListColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) { items(12) { Chip2("Item ${it + 1}", p.accent, Modifier.fillMaxWidth()) } }
            }
        },
        Demo(
            "Layout", "ListRow", "Compose", "LazyRow without the legacy overscroll effect, built with a LazyListScope.",
            """ListRow {
    items(20) { Text("Item ${'$'}it") }
}"""
        ) {
            val p = LocalPalette.current
            ListRow(modifier = narrow2(), horizontalArrangement = Arrangement.spacedBy(6.dp)) { items(12) { Chip2("Item ${it + 1}", p.mint) } }
        },
        Demo(
            "Layout", "ComposePagerSnapHelper", "Compose", "Gives a LazyRow snap-to-page behavior using the width of each item.",
            """ComposePagerSnapHelper(width = 200.dp) { listState ->
    LazyRow(state = listState) {
        items(5) { Card(Modifier.width(200.dp)) }
    }
}"""
        ) {
            val p = LocalPalette.current
            ComposePagerSnapHelper(width = 200.dp) { listState ->
                LazyRow(state = listState, modifier = narrow2()) {
                    items(5) { i -> Box(Modifier.width(200.dp).height(70.dp).padding(horizontal = 4.dp).clip(RoundedCornerShape(12.dp)).background(if (i % 2 == 0) p.accent else p.mint), contentAlignment = Alignment.Center) { Text("Page ${i + 1}", color = p.onAccent, fontWeight = FontWeight.Bold) } }
                }
            }
        },
        Demo(
            "Loading", "ProgressIndicator", "Compose", "Orbiting dots indicator; DotsLoading wraps it to fill its container.",
            """ProgressIndicator(
    modifier = Modifier.size(50.dp),
    color = accent
)"""
        ) {
            val p = LocalPalette.current
            Box(Modifier.size(80.dp)) { ProgressIndicator(modifier = Modifier.fillMaxSize(), color = p.accent) }
        },
        Demo(
            "Text", "DashedDivider", "Compose", "Divider drawn as a dashed line, with configurable dash width and gap.",
            """DashedDivider(
    color = accent,
    dashWidth = 12f,
    dashGap = 8f,
    height = 2.dp
)"""
        ) {
            val p = LocalPalette.current
            var gap by remember { mutableStateOf(8f) }
            Column(narrow2(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                DashedDivider(color = p.accent, dashWidth = 12f, dashGap = gap, height = 2.dp)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(4f, 8f, 20f).forEach { g -> Pill("gap ${g.toInt()}", gap == g, { gap = g }) }
                }
            }
        },
        Demo(
            "States", "ScaffoldStates", "Compose", "Scaffold that renders loading, success or failure content from a list of items.",
            """ScaffoldStates(
    isLoading = state.isLoading,
    items = state.items,
    onLoading = { padding -> Loader(padding) },
    onSuccess = { padding, items -> List(padding, items) },
    onFailed = { padding -> Error(padding) }
)"""
        ) {
            val p = LocalPalette.current
            var mode by remember { mutableStateOf("success") }
            Column(narrow2(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) { listOf("loading", "success", "empty").forEach { m -> Pill(m, mode == m, { mode = m }) } }
                Box(Modifier.fillMaxWidth().height(120.dp).clip(RoundedCornerShape(12.dp))) {
                    ScaffoldStates(
                        isLoading = mode == "loading", items = if (mode == "success") listOf("Alpha", "Beta", "Gamma") else emptyList<String>(),
                        containerColor = p.panel,
                        onLoading = { pad -> Box(Modifier.fillMaxSize().padding(pad), contentAlignment = Alignment.Center) { Text("Loading…", color = p.fg) } },
                        onSuccess = { pad, items -> Column(Modifier.padding(pad).padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) { items.forEach { Chip2(it, p.accent, Modifier.fillMaxWidth()) } } },
                        onFailed = { pad -> Box(Modifier.fillMaxSize().padding(pad), contentAlignment = Alignment.Center) { Text("Nothing to show", color = p.muted) } }
                    )
                }
            }
        }
    )
}
