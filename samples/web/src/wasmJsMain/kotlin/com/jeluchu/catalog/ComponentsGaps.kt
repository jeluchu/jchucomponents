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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.ui.unit.sp
import com.jeluchu.jchucomponents.ui.animations.lists.Animations
import com.jeluchu.jchucomponents.ui.animations.lists.animateItem
import com.jeluchu.jchucomponents.ui.composables.images.jchuSnapBackZoomable
import com.jeluchu.jchucomponents.ui.composables.images.jchuZoomable
import com.jeluchu.jchucomponents.ui.composables.images.rememberJchuZoomState
import com.jeluchu.jchucomponents.ui.composables.images.toggleScale
import com.jeluchu.jchucomponents.ui.composables.snackbar.SnackbarController
import com.jeluchu.jchucomponents.ui.composables.snackbar.showSnack
import com.jeluchu.jchucomponents.ui.composables.swipe.SwipeAction
import com.jeluchu.jchucomponents.ui.composables.swipe.SwipeableActionsBox
import com.jeluchu.jchucomponents.ui.composables.swipe.rememberSwipeableActionsState
import com.jeluchu.jchucomponents.ui.dragndrop.ReorderableItem
import com.jeluchu.jchucomponents.ui.dragndrop.animateDraggeableItemPlacement
import com.jeluchu.jchucomponents.ui.dragndrop.extensions.detectReorder
import com.jeluchu.jchucomponents.ui.dragndrop.extensions.detectReorderAfterLongPress
import com.jeluchu.jchucomponents.ui.dragndrop.reorderable
import com.jeluchu.jchucomponents.ui.dragndrop.states.rememberReorderableLazyGridState
import com.jeluchu.jchucomponents.ui.dragndrop.states.rememberReorderableLazyListState
import com.jeluchu.jchucomponents.ui.dragndrop.states.rememberReorderableLazyVerticalStaggeredGridState
import com.jeluchu.jchucomponents.ui.extensions.modifier.disableHorizontalScroll
import com.jeluchu.jchucomponents.ui.extensions.modifier.disableVerticalScroll
import com.jeluchu.jchucomponents.ui.extensions.modifier.interceptionClickable
import com.jeluchu.jchucomponents.ui.foundation.lists.composition.ListNotOverScroll
import com.jeluchu.jchucomponents.ui.foundation.text.drawFadedEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.foundation.lazy.grid.items as gridItems

private fun narrow3() = Modifier.widthIn(max = 300.dp).fillMaxWidth()

@Composable
private fun Cell(label: String, color: Color, modifier: Modifier = Modifier) {
    val p = LocalPalette.current
    Box(modifier.clip(RoundedCornerShape(10.dp)).background(color).padding(12.dp), contentAlignment = Alignment.Center) {
        Text(label, color = p.onAccent, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}

val gapDemos: List<Demo> by lazy {
    listOf(
        Demo(
            "Gestures", "ReorderableItem", "Compose", "Reorder LazyColumn items by long-pressing and dragging (reorderable list state).",
            """val data = remember { mutableStateListOf("A", "B", "C") }
val state = rememberReorderableLazyListState(
    onMove = { from, to -> data.add(to.index, data.removeAt(from.index)) }
)

LazyColumn(state = state.listState, modifier = Modifier.reorderable(state)) {
    items(data, { it }) { item ->
        ReorderableItem(state, key = item) { isDragging ->
            Row(Modifier.detectReorderAfterLongPress(state)) { Text(item) }
        }
    }
}"""
        ) {
            val p = LocalPalette.current
            val data = remember { mutableStateListOf("Alpha", "Beta", "Gamma", "Delta") }
            val state = rememberReorderableLazyListState(onMove = { from, to -> data.add(to.index, data.removeAt(from.index)) })
            LazyColumn(state = state.listState, modifier = narrow3().height(170.dp).reorderable(state), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                items(data, { it }) { item ->
                    ReorderableItem(state, key = item) { isDragging ->
                        Cell(if (isDragging) "$item (moving)" else "$item · hold and drag", if (isDragging) p.mint else p.accent, Modifier.fillMaxWidth().detectReorderAfterLongPress(state))
                    }
                }
            }
        },
        Demo(
            "Gestures", "rememberReorderableLazyGridState", "Compose", "Reorder grid items by dragging; detectReorder starts the drag immediately.",
            """val state = rememberReorderableLazyGridState(
    onMove = { from, to -> data.add(to.index, data.removeAt(from.index)) }
)

LazyVerticalGrid(GridCells.Fixed(3), state = state.gridState, modifier = Modifier.reorderable(state)) {
    items(data, { it }) { item ->
        ReorderableItem(state, key = item) { Cell(Modifier.detectReorder(state)) }
    }
}"""
        ) {
            val p = LocalPalette.current
            val data = remember { mutableStateListOf("1", "2", "3", "4", "5", "6") }
            val state = rememberReorderableLazyGridState(onMove = { from, to -> data.add(to.index, data.removeAt(from.index)) })
            LazyVerticalGrid(GridCells.Fixed(3), state = state.gridState, modifier = narrow3().height(150.dp).reorderable(state), verticalArrangement = Arrangement.spacedBy(6.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                gridItems(data, { it }) { item ->
                    ReorderableItem(state, key = item) { isDragging ->
                        Cell(item, if (isDragging) p.mint else p.accent, Modifier.fillMaxWidth().detectReorder(state).animateDraggeableItemPlacement())
                    }
                }
            }
        },
        Demo(
            "Gestures", "rememberReorderableLazyVerticalStaggeredGridState", "Compose", "Reorder staggered grid items; the horizontal and generic staggered states work the same way.",
            """val state = rememberReorderableLazyVerticalStaggeredGridState(
    onMove = { from, to -> data.add(to.index, data.removeAt(from.index)) }
)
// also: rememberReorderableLazyHorizontalStaggeredGridState and
// rememberReorderableLazyStaggeredGridState (orientation as a parameter)

LazyVerticalStaggeredGrid(StaggeredGridCells.Fixed(3), state = state.gridState, modifier = Modifier.reorderable(state)) {
    items(data, { it }) { item -> ReorderableItem(state, key = item) { Cell(Modifier.detectReorder(state)) } }
}"""
        ) {
            val p = LocalPalette.current
            val data = remember { mutableStateListOf("A", "B", "C", "D", "E", "F") }
            val state = rememberReorderableLazyVerticalStaggeredGridState(onMove = { from, to -> data.add(to.index, data.removeAt(from.index)) })
            LazyVerticalStaggeredGrid(
                StaggeredGridCells.Fixed(3), state = state.gridState, modifier = narrow3().height(160.dp).reorderable(state),
                verticalItemSpacing = 6.dp, horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(data, { it }) { item ->
                    ReorderableItem(state, key = item) { isDragging ->
                        val h = if (item in listOf("A", "D")) 64.dp else 40.dp
                        Cell(item, if (isDragging) p.mint else p.accent, Modifier.fillMaxWidth().height(h).detectReorder(state))
                    }
                }
            }
        },
        Demo(
            "Gestures", "animateItem", "Compose", "Animates list items in and out, with fade, scale, slide and rotate presets.",
            """LazyColumn {
    items(list, key = { it }) { item ->
        Row(Modifier.animateItem(Animations.FadeAndSlide)) { Text(item) }
    }
}"""
        ) {
            val p = LocalPalette.current
            var kind by remember { mutableStateOf(Animations.FadeAndSlide) }
            var n by remember { mutableStateOf(3) }
            Column(narrow3(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(Animations.Fade, Animations.Scale, Animations.FadeAndSlide).forEach { a -> Pill(a.name, kind == a, { kind = a }) }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) { Pill("Add", false, { n++ }); Pill("Remove", false, { if (n > 0) n-- }) }
                androidx.compose.runtime.key(kind) {
                    LazyColumn(Modifier.fillMaxWidth().height(110.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        items((1..n).toList(), { it }) { i -> Cell("Item $i", p.accent, Modifier.fillMaxWidth().animateItem(kind)) }
                    }
                }
            }
        },
        Demo(
            "Gestures", "jchuSnapBackZoomable", "Compose", "Zoom that springs back to its original size when you release; toggleScale zooms programmatically.",
            """val zoom = rememberJchuZoomState()

Box(Modifier.jchuSnapBackZoomable(zoom)) { Image(...) }

scope.launch { zoom.toggleScale(2.5f, center) }"""
        ) {
            val p = LocalPalette.current
            val zoom = rememberJchuZoomState()
            val scope = rememberCoroutineScope()
            Column(narrow3(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.fillMaxWidth().height(110.dp).clip(RoundedCornerShape(12.dp)).background(p.panel)) {
                    Box(Modifier.fillMaxSize().jchuSnapBackZoomable(zoom).background(p.soft), contentAlignment = Alignment.Center) {
                        Text("Pinch or scroll: it snaps back", color = p.softFg, fontWeight = FontWeight.Bold)
                    }
                }
                Pill("toggleScale(2.5f)", false, { scope.launch { zoom.toggleScale(2.5f, Offset(150f, 100f)) } }, mono = true)
            }
        },
        Demo(
            "Gestures", "disableVerticalScroll", "Compose", "Blocks vertical scrolling of everything inside; disableHorizontalScroll does the same sideways.",
            """LazyColumn(Modifier.disableVerticalScroll()) {
    items(20) { Text("Item ${'$'}it") }
}"""
        ) {
            val p = LocalPalette.current
            var blocked by remember { mutableStateOf(true) }
            var horizontal by remember { mutableStateOf(false) }
            Column(narrow3(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Pill(if (blocked) "Vertical blocked" else "Vertical free", blocked, { blocked = !blocked })
                    Pill(if (horizontal) "Horizontal blocked" else "Horizontal free", horizontal, { horizontal = !horizontal })
                }
                Box(
                    Modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(12.dp)).background(p.panel)
                        .then(if (blocked) Modifier.disableVerticalScroll() else Modifier)
                        .then(if (horizontal) Modifier.disableHorizontalScroll() else Modifier)
                ) {
                    LazyColumn(Modifier.fillMaxSize().padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) { items(12) { Cell("Item ${it + 1}", p.accent, Modifier.fillMaxWidth()) } }
                }
            }
        },
        Demo(
            "Gestures", "interceptionClickable", "Compose", "Swallows clicks without a ripple, so an overlay can block what is behind it.",
            """Box {
    Content()
    if (loading) Box(Modifier.fillMaxSize().interceptionClickable())
}"""
        ) {
            val p = LocalPalette.current
            var overlay by remember { mutableStateOf(true) }
            var taps by remember { mutableStateOf(0) }
            Column(narrow3(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Pill(if (overlay) "Overlay on" else "Overlay off", overlay, { overlay = !overlay })
                Box(Modifier.fillMaxWidth().height(70.dp).clip(RoundedCornerShape(12.dp)).background(p.panel), contentAlignment = Alignment.Center) {
                    Pill("Button behind · $taps", false, { taps++ }, height = 44.dp)
                    if (overlay) Box(Modifier.fillMaxSize().background(p.bg.copy(alpha = 0.45f)).interceptionClickable())
                }
            }
        },
        Demo(
            "Effects", "drawFadedEdge", "Compose", "Fades the left or right edge of any content; MarqueeText uses it internally.",
            """Modifier
    .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
    .drawWithContent {
        drawContent()
        drawFadedEdge(leftEdge = true, edgeWidth = 24.dp)
        drawFadedEdge(leftEdge = false, edgeWidth = 24.dp)
    }"""
        ) {
            val p = LocalPalette.current
            Box(
                narrow3().height(46.dp).graphicsLayer { compositingStrategy = androidx.compose.ui.graphics.CompositingStrategy.Offscreen }
                    .drawWithContent { drawContent(); drawFadedEdge(leftEdge = true, edgeWidth = 36.dp); drawFadedEdge(leftEdge = false, edgeWidth = 36.dp) }
                    .background(p.accent), contentAlignment = Alignment.Center
            ) { Text("Both edges fade out to transparent", color = p.onAccent, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1) }
        },
        Demo(
            "Feedback", "SnackbarController", "Compose", "Shows snackbars one at a time: a new one replaces the one on screen.",
            """val host = remember { SnackbarHostState() }
val controller = remember { SnackbarController(scope) }

controller.showSnack(host, "Saved to library")"""
        ) {
            val p = LocalPalette.current
            val host = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()
            val controller = remember { SnackbarController(scope) }
            var n by remember { mutableStateOf(0) }
            Box(narrow3().height(150.dp)) {
                Box(Modifier.align(Alignment.TopCenter)) { Pill("showSnack (replaces previous)", false, { controller.showSnack(host, "Message ${++n}") }, height = 44.dp) }
                SnackbarHost(host, Modifier.align(Alignment.BottomCenter))
            }
        },
        Demo(
            "Layout", "ListNotOverScroll", "Compose", "Removes the platform overscroll effect from the lists inside it.",
            """ListNotOverScroll {
    LazyColumn { items(20) { Text("Item ${'$'}it") } }
}"""
        ) {
            val p = LocalPalette.current
            Box(narrow3().height(110.dp)) {
                ListNotOverScroll { LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(4.dp)) { items(12) { Cell("Item ${it + 1}", p.accent, Modifier.fillMaxWidth()) } } }
            }
        }
    )
}
