@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class, androidx.compose.foundation.ExperimentalFoundationApi::class)

package com.jeluchu.catalog

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.jeluchu.jchucomponents.ui.composables.cards.Card
import com.jeluchu.jchucomponents.ui.composables.cards.CardColors
import com.jeluchu.jchucomponents.ui.composables.chips.RemovableChip
import com.jeluchu.jchucomponents.ui.composables.chips.Type
import com.jeluchu.jchucomponents.ui.composables.chips.TypeColors
import com.jeluchu.jchucomponents.ui.composables.column.JchuScrollableColumn
import com.jeluchu.jchucomponents.ui.composables.images.jchuZoomable
import com.jeluchu.jchucomponents.ui.composables.images.rememberJchuZoomState
import com.jeluchu.jchucomponents.ui.composables.pager.Pager
import com.jeluchu.jchucomponents.ui.composables.pager.PagerState
import com.jeluchu.jchucomponents.ui.composables.particles.Particles
import com.jeluchu.jchucomponents.ui.composables.preferences.PreferenceSwitchWithContainer
import com.jeluchu.jchucomponents.ui.composables.preferences.PreferencesHintCard
import com.jeluchu.jchucomponents.ui.composables.preferences.SwitchWithContainerColors
import com.jeluchu.jchucomponents.ui.composables.sheets.BottomSheetSettings
import com.jeluchu.jchucomponents.ui.composables.sheets.BottomSheetWithCloseDialog
import com.jeluchu.jchucomponents.ui.composables.shimmer.MiniPostItemShimmer
import com.jeluchu.jchucomponents.ui.composables.shimmer.MovieItemShimmer
import com.jeluchu.jchucomponents.ui.composables.shimmer.ShimmerItemBig
import com.jeluchu.jchucomponents.ui.composables.structures.JchuDefaultEmptyContent
import com.jeluchu.jchucomponents.ui.composables.structures.JchuDefaultLoadingContent
import com.jeluchu.jchucomponents.ui.composables.structures.JchuRemoteScreenContent
import com.jeluchu.jchucomponents.ui.composables.swipe.SwipeAction
import com.jeluchu.jchucomponents.ui.composables.swipe.SwipeableActionsBox
import com.jeluchu.jchucomponents.ui.composables.swipe.rememberSwipeableActionsState
import com.jeluchu.jchucomponents.ui.composables.toolbars.CenterToolbar
import com.jeluchu.jchucomponents.ui.composables.toolbars.CenterToolbarColors
import com.jeluchu.jchucomponents.ui.composables.toolbars.Toolbar
import com.jeluchu.jchucomponents.ui.foundation.icon.IconLink
import com.jeluchu.jchucomponents.ui.foundation.lists.DraggableItem
import com.jeluchu.jchucomponents.ui.foundation.lists.GridCells
import com.jeluchu.jchucomponents.ui.foundation.lists.LazyColumnFor
import com.jeluchu.jchucomponents.ui.foundation.lists.LazyGrid
import com.jeluchu.jchucomponents.ui.foundation.lists.LazyHorizontalGrid
import com.jeluchu.jchucomponents.ui.foundation.lists.LazyStaticGrid
import com.jeluchu.jchucomponents.ui.foundation.lists.dragContainer
import com.jeluchu.jchucomponents.ui.foundation.lists.rememberDragDropState
import com.jeluchu.jchucomponents.ui.foundation.text.MarqueeText

private fun narrow() = Modifier.widthIn(max = 300.dp).fillMaxWidth()

@Composable
private fun Tile(label: String, color: Color, modifier: Modifier = Modifier) {
    val p = LocalPalette.current
    Box(modifier.clip(RoundedCornerShape(10.dp)).background(color).padding(10.dp), contentAlignment = Alignment.Center) {
        Text(label, color = p.onAccent, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}

val moreDemos: List<Demo> by lazy {
    listOf(
        // ---------- Cards
        Demo(
            "Cards", "Card", "Compose", "Clickable card with colors, border, elevation and a Column content slot.",
            """Card(
    onClick = { open() },
    colors = CardColors(containerColor = soft, contentColor = fg)
) {
    Text("Tap me")
}"""
        ) {
            val p = LocalPalette.current
            var taps by remember { mutableStateOf(0) }
            Card(onClick = { taps++ }, modifier = narrow(), colors = CardColors(containerColor = p.soft, contentColor = p.softFg)) {
                Text("Tap me", fontWeight = FontWeight.ExtraBold)
                Text("Tapped $taps times")
            }
        },
        // ---------- Navigation
        Demo(
            "Navigation", "Toolbar", "Compose", "Top app bar with navigation icon, title and action slots.",
            """Toolbar(
    title = { Text("Library") },
    navigationIcon = { IconLink(Icons.Default.ArrowBack, "Back") { } },
    actions = { IconLink(Icons.Default.Search, "Search") { } }
)"""
        ) {
            val p = LocalPalette.current
            Box(narrow().clip(RoundedCornerShape(12.dp))) {
                Toolbar(
                    title = { Text("Library") },
                    navigationIcon = { IconLink(Icons.Default.ArrowBack, "Back", tint = p.fg) { } },
                    actions = { IconLink(Icons.Default.Search, "Search", tint = p.fg) { } },
                    colors = CenterToolbarColors(containerColor = p.panel, contentColor = p.fg)
                )
            }
        },
        Demo(
            "Navigation", "CenterToolbar", "Compose", "Top app bar with a centered title.",
            """CenterToolbar(
    title = { Text("Catalog") },
    navigationIcon = { IconLink(Icons.Default.Menu, "Menu") { } },
    actions = { IconLink(Icons.Default.Share, "Share") { } }
)"""
        ) {
            val p = LocalPalette.current
            Box(narrow().clip(RoundedCornerShape(12.dp))) {
                CenterToolbar(
                    title = { Text("Catalog") },
                    navigationIcon = { IconLink(Icons.Default.Menu, "Menu", tint = p.fg) { } },
                    actions = { IconLink(Icons.Default.Share, "Share", tint = p.fg) { } },
                    colors = CenterToolbarColors(containerColor = p.panel, contentColor = p.fg)
                )
            }
        },
        Demo(
            "Navigation", "Pager", "Compose", "Horizontal pager driven by a PagerState.",
            """val state = remember { PagerState(currentPage = 0, minPage = 0, maxPage = 2) }

Pager(state = state) {
    Text("Page ${'$'}{page + 1}")
}"""
        ) {
            val p = LocalPalette.current
            val state = remember { PagerState(currentPage = 0, minPage = 0, maxPage = 2) }
            Box(narrow().height(110.dp).clip(RoundedCornerShape(12.dp)).background(p.panel)) {
                Pager(state = state) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Page ${page + 1} of 3 · drag to swipe", color = p.fg) }
                }
            }
        },
        // ---------- Surfaces
        Demo(
            "Surfaces", "BottomSheetWithCloseDialog", "Compose", "Sheet content with a drag handle and close affordances.",
            """BottomSheetWithCloseDialog(
    bottomSheetSettings = BottomSheetSettings(
        isCloseIconShow = true,
        onClosePressed = { dismiss() }
    )
) {
    Text("Content")
}"""
        ) {
            val p = LocalPalette.current
            Box(narrow().clip(RoundedCornerShape(14.dp)).background(p.panel).padding(bottom = 12.dp)) {
                BottomSheetWithCloseDialog(bottomSheetSettings = BottomSheetSettings(buttonTint = p.muted, isCloseIconShow = true)) {
                    Text("Sheet content", color = p.fg, modifier = Modifier.padding(16.dp))
                }
            }
        },
        // ---------- Layout
        Demo(
            "Layout", "JchuScrollableColumn", "Compose", "Vertical scrolling column that keeps ColumnScope for its children.",
            """JchuScrollableColumn(
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    repeat(8) { Text("Row ${'$'}it") }
}"""
        ) {
            val p = LocalPalette.current
            Box(narrow().height(130.dp).clip(RoundedCornerShape(12.dp)).background(p.panel)) {
                JchuScrollableColumn(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(8.dp)) {
                    repeat(8) { Tile("Row ${it + 1}", p.accent, Modifier.fillMaxWidth()) }
                }
            }
        },
        Demo(
            "Layout", "LazyStaticGrid", "Compose", "Grid built from rows, filling the last row so items keep their width.",
            """LazyStaticGrid(
    columns = 3,
    itemCount = 7
) { index ->
    Tile("${'$'}index")
}"""
        ) {
            val p = LocalPalette.current
            LazyStaticGrid(modifier = narrow(), columns = 3, itemCount = 7) { index ->
                Tile("${index + 1}", p.accent, Modifier.weight(1f).padding(3.dp))
            }
        },
        Demo(
            "Layout", "LazyHorizontalGrid", "Compose", "Horizontally scrolling grid with fixed rows.",
            """LazyHorizontalGrid(cells = GridCells.Fixed(2)) {
    items(10) { Tile("${'$'}it") }
}"""
        ) {
            val p = LocalPalette.current
            LazyHorizontalGrid(cells = GridCells.Fixed(2), modifier = narrow().height(110.dp)) {
                items(10) { Tile("${it + 1}", if (it % 2 == 0) p.accent else p.mint, Modifier.padding(3.dp).width(70.dp).fillMaxHeight()) }
            }
        },
        Demo(
            "Layout", "LazyGrid", "Compose", "Lazy list that groups items into rows of a given size.",
            """LazyGrid(
    items = list,
    rows = 3
) { item, index ->
    Tile(item)
}"""
        ) {
            val p = LocalPalette.current
            val list = remember { List(9) { "${it + 1}" } }
            Box(narrow().height(130.dp)) {
                LazyGrid(items = list, rows = 3) { item, _ -> Tile(item, p.accent, Modifier.padding(3.dp).width(72.dp)) }
            }
        },
        Demo(
            "Layout", "LazyColumnFor", "Compose", "LazyColumn that takes an Array of items directly.",
            """LazyColumnFor(items = arrayOf("A", "B", "C")) { item ->
    Text(item)
}"""
        ) {
            val p = LocalPalette.current
            Box(narrow().height(130.dp)) {
                LazyColumnFor(items = arrayOf("One", "Two", "Three", "Four", "Five", "Six")) { item ->
                    Tile(item, p.accent, Modifier.fillMaxWidth().padding(vertical = 3.dp))
                }
            }
        },
        Demo(
            "Layout", "DraggableItem", "Compose", "Reorder LazyColumn items by dragging them.",
            """val items = remember { mutableStateListOf("A", "B", "C") }
val listState = rememberLazyListState()
val dragDropState = rememberDragDropState(listState) { from, to ->
    items.add(to, items.removeAt(from))
}

LazyColumn(Modifier.dragContainer(dragDropState), state = listState) {
    itemsIndexed(items) { index, item ->
        DraggableItem(dragDropState, index) { isDragging -> Text(item) }
    }
}"""
        ) {
            val p = LocalPalette.current
            val items = remember { mutableStateListOf("Alpha", "Beta", "Gamma", "Delta") }
            val listState = rememberLazyListState()
            val dragDropState = rememberDragDropState(listState) { from, to -> items.add(to, items.removeAt(from)) }
            LazyColumn(narrow().height(170.dp).dragContainer(dragDropState), state = listState, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                itemsIndexed(items, key = { _, it -> it }) { index, item ->
                    DraggableItem(dragDropState, index) { isDragging ->
                        Tile(if (isDragging) "$item (moving)" else "$item  ⋮", if (isDragging) p.mint else p.accent, Modifier.fillMaxWidth())
                    }
                }
            }
        },
        // ---------- Gestures
        Demo(
            "Gestures", "SwipeableActionsBox", "Compose", "Reveals actions when you swipe the content to either side.",
            """val state = rememberSwipeableActionsState()

SwipeableActionsBox(
    state = state,
    startActions = listOf(SwipeAction(onSwipe = { fav() }, icon = { Icon(...) }, background = mint)),
    endActions = listOf(SwipeAction(onSwipe = { delete() }, icon = { Icon(...) }, background = red))
) {
    Text("Swipe me")
}"""
        ) {
            val p = LocalPalette.current
            var last by remember { mutableStateOf("none") }
            val swipeState = rememberSwipeableActionsState()
            Column(narrow(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SwipeableActionsBox(
                    state = swipeState,
                    startActions = listOf(SwipeAction(onSwipe = { last = "favorite" }, icon = { Icon(Icons.Default.Favorite, null, tint = Color(0xFF1B2128)) }, background = p.mint)),
                    endActions = listOf(SwipeAction(onSwipe = { last = "delete" }, icon = { Icon(Icons.Default.Delete, null, tint = Color.White) }, background = Color(0xFFB3543F)))
                ) {
                    Box(Modifier.fillMaxWidth().background(p.panel).padding(18.dp)) { Text("Swipe me left or right", color = p.fg) }
                }
                Text("Last action: $last · offset ${swipeState.offset.value.toInt()}px", color = p.muted, fontSize = 12.sp)
            }
        },
        Demo(
            "Gestures", "jchuZoomable", "Compose", "Pinch, pan and double-tap zoom for any content.",
            """val zoomState = rememberJchuZoomState()

Box(Modifier.jchuZoomable(zoomState)) {
    Image(...)
}"""
        ) {
            val p = LocalPalette.current
            val zoom = rememberJchuZoomState()
            Box(narrow().height(130.dp).clip(RoundedCornerShape(12.dp)).background(p.panel)) {
                Box(Modifier.fillMaxSize().jchuZoomable(zoom).background(p.soft), contentAlignment = Alignment.Center) {
                    Text("Double-click to zoom", color = p.softFg, fontWeight = FontWeight.Bold)
                }
            }
        },
        // ---------- States
        Demo(
            "States", "JchuRemoteScreenContent", "Compose", "Switches between loading, success and failure for remote data.",
            """JchuRemoteScreenContent(
    data = state.data,
    isLoading = state.isLoading,
    error = state.error,
    onLoading = { JchuDefaultLoadingContent() },
    onSuccess = { data -> Text(data) },
    onFailure = { message -> Text(message ?: "Error") }
)"""
        ) {
            val p = LocalPalette.current
            var mode by remember { mutableStateOf("loading") }
            Column(narrow(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("loading", "success", "error").forEach { m -> Pill(m, mode == m, { mode = m }) }
                }
                Box(Modifier.fillMaxWidth().height(90.dp), contentAlignment = Alignment.Center) {
                    JchuRemoteScreenContent(
                        data = if (mode == "success") "Loaded 24 items" else null,
                        isLoading = mode == "loading", error = if (mode == "error") "Network unavailable" else null,
                        onLoading = { JchuDefaultLoadingContent() },
                        onSuccess = { Text(it, color = p.fg, fontWeight = FontWeight.Bold) },
                        onFailure = { Text(it ?: "Error", color = p.muted) }
                    )
                }
            }
        },
        Demo(
            "States", "JchuDefaultEmptyContent", "Compose", "Ready-made empty state with a message.",
            """JchuDefaultEmptyContent(text = "No items yet")"""
        ) {
            JchuDefaultEmptyContent(text = "No items yet")
        },
        Demo(
            "States", "JchuDefaultLoadingContent", "Compose", "Ready-made loading state.",
            """JchuDefaultLoadingContent()"""
        ) {
            JchuDefaultLoadingContent()
        },
        Demo(
            "States", "ShimmerItemBig", "Compose", "Animated placeholder for a large list item.",
            """val transition = rememberInfiniteTransition()
val anim by transition.animateFloat(0f, 1000f, infiniteRepeatable(tween(1200)))

ShimmerItemBig(
    lists = listOf(gray, white, gray),
    floatAnim = anim,
    isVertical = false
)"""
        ) {
            val p = LocalPalette.current
            val anim by shimmerAnim()
            Box(narrow()) { ShimmerItemBig(lists = listOf(p.line, p.panel, p.line), floatAnim = anim, isVertical = false) }
        },
        Demo(
            "States", "MovieItemShimmer", "Compose", "Animated placeholder shaped like a movie poster row.",
            """MovieItemShimmer(
    lists = listOf(gray, white, gray),
    floatAnim = anim,
    isVertical = false
)"""
        ) {
            val p = LocalPalette.current
            val anim by shimmerAnim()
            Box(narrow()) { MovieItemShimmer(lists = listOf(p.line, p.panel, p.line), floatAnim = anim, isVertical = false) }
        },
        Demo(
            "States", "MiniPostItemShimmer", "Compose", "Compact animated placeholder for post items.",
            """MiniPostItemShimmer(
    lists = listOf(gray, white, gray),
    floatAnim = anim,
    isVertical = false
)"""
        ) {
            val p = LocalPalette.current
            val anim by shimmerAnim()
            Box(narrow()) { MiniPostItemShimmer(lists = listOf(p.line, p.panel, p.line), floatAnim = anim, isVertical = false) }
        },
        // ---------- Text and chips
        Demo(
            "Text", "MarqueeText", "Compose", "Single-line text that scrolls when it does not fit, with faded edges.",
            """MarqueeText(
    text = "A very long title that does not fit",
    gradientEdgeColor = background
)"""
        ) {
            val p = LocalPalette.current
            MarqueeText(
                text = "The world is a vampire! And this text is long enough to scroll across the card",
                modifier = narrow(), gradientEdgeColor = p.stage, color = p.fg
            )
        },
        Demo(
            "Chips", "Type", "Compose", "Marquee label chip for categories such as TV or OVA.",
            """Type(
    text = "Spring 2026 · Finished airing",
    colors = TypeColors(content = accent, gradientEdge = bg, container = soft)
)"""
        ) {
            val p = LocalPalette.current
            Type(text = "Spring 2026 · Finished airing · 24 episodes", modifier = Modifier.widthIn(max = 220.dp), colors = TypeColors(content = p.accent, gradientEdge = p.stage, container = p.soft))
        },
        Demo(
            "Chips", "RemovableChip", "Compose", "Chip with a trailing remove icon.",
            """RemovableChip(
    label = "Action",
    contentDescription = "Remove Action",
    onRemove = { tags.remove("Action") }
)"""
        ) {
            val p = LocalPalette.current
            val tags = remember { mutableStateListOf("Action", "Drama", "Sci-Fi") }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                tags.toList().forEach { t -> RemovableChip(label = t, contentDescription = "Remove $t", color = p.accent, textColor = p.onAccent, onRemove = { tags.remove(t) }) }
                if (tags.isEmpty()) Pill("Reset", false, { tags.addAll(listOf("Action", "Drama", "Sci-Fi")) })
            }
        },
        Demo(
            "Chips", "IconLink", "Compose", "Icon button for a vector or bitmap.",
            """IconLink(
    imageVector = Icons.Default.Add,
    contentDescription = "Add",
    onClick = { add() }
)"""
        ) {
            val p = LocalPalette.current
            var n by remember { mutableStateOf(0) }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconLink(Icons.Default.Add, "Add", tint = p.accent) { n++ }
                Text("clicked $n times", color = p.fg)
            }
        },
        // ---------- Effects
        Demo(
            "Effects", "Particles", "Compose", "Emoji particles that float up while visible is true.",
            """Particles(
    modifier = Modifier.fillMaxSize(),
    quantity = 12,
    emoji = "✨",
    visible = show
)"""
        ) {
            var show by remember { mutableStateOf(false) }
            Box(narrow().height(130.dp), contentAlignment = Alignment.Center) {
                Pill(if (show) "Hide" else "Celebrate", show, { show = !show }, height = 44.dp)
                Particles(modifier = Modifier.fillMaxSize(), quantity = 10, emoji = "*", visible = show)
            }
        },
        // ---------- Preferences (legacy variants)
        Demo(
            "Preferences", "PreferenceSwitchWithContainer", "Compose", "Switch row whose whole container changes color.",
            """PreferenceSwitchWithContainer(
    title = "Dark mode",
    isChecked = on,
    onClick = { on = !on }
)"""
        ) {
            val p = LocalPalette.current
            var on by remember { mutableStateOf(true) }
            PreferenceSwitchWithContainer(
                title = "Dark mode", isChecked = on, onClick = { on = !on },
                colors = SwitchWithContainerColors(iconEnabled = p.onAccent, iconDisabled = p.fg, textEnabled = p.onAccent, textDisabled = p.fg, backgroundEnabled = p.accent, backgroundDisabled = p.panel)
            )
        },
        Demo(
            "Preferences", "PreferencesHintCard", "Compose", "Hint card with an icon, title and description.",
            """PreferencesHintCard(
    title = "Language",
    description = "Choose the app language",
    onClick = { }
)"""
        ) {
            PreferencesHintCard(title = "Language", description = "Choose the app language", isDarkTheme = LocalPalette.current.dark)
        }
    )
}

@Composable
private fun shimmerAnim(): androidx.compose.runtime.State<Float> =
    rememberInfiniteTransition().animateFloat(0f, 1000f, infiniteRepeatable(tween(1200)))
