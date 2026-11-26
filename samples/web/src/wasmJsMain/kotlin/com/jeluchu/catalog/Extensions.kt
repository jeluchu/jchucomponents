package com.jeluchu.catalog

import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.material.icons.filled.Star
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeluchu.jchucomponents.foundation.extensions.milliSecondsToTimer
import com.jeluchu.jchucomponents.foundation.extensions.thousandsFormat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.Spacer
import com.jeluchu.jchucomponents.ui.composables.accessibility.calculateContrastFor
import com.jeluchu.jchucomponents.ui.composables.accessibility.isLightColor
import com.jeluchu.jchucomponents.ui.composables.accessibility.onColor
import com.jeluchu.jchucomponents.ui.extensions.color.isDark
import com.jeluchu.jchucomponents.ui.extensions.color.orTransparent
import com.jeluchu.jchucomponents.ui.extensions.color.random
import com.jeluchu.jchucomponents.ui.extensions.lists.itemIf
import com.jeluchu.jchucomponents.ui.extensions.modifier.Height
import com.jeluchu.jchucomponents.ui.extensions.modifier.Width
import com.jeluchu.jchucomponents.ui.extensions.modifier.graphicsCollapse
import com.jeluchu.jchucomponents.ui.extensions.color.toColorFilter
import com.jeluchu.jchucomponents.ui.extensions.lists.disableScrolling
import com.jeluchu.jchucomponents.ui.extensions.lists.enableScrolling
import com.jeluchu.jchucomponents.ui.extensions.withHorizontalPadding
import com.jeluchu.jchucomponents.ui.extensions.modifier.click
import com.jeluchu.jchucomponents.ui.extensions.modifier.cornerRadius
import com.jeluchu.jchucomponents.ui.extensions.modifier.noRippleClickable
import com.jeluchu.jchucomponents.ui.extensions.modifier.whenNotNull
import com.jeluchu.jchucomponents.ui.modifiers.dashedBorder
import com.jeluchu.jchucomponents.ui.extensions.color.opacity
import com.jeluchu.jchucomponents.ui.extensions.modifier.bounceClick
import com.jeluchu.jchucomponents.ui.extensions.modifier.conditional
import com.jeluchu.jchucomponents.ui.extensions.modifier.shake

@Composable
fun ExtensionsTab() {
    val p = LocalPalette.current
    FlowRow(horizontalArrangement = Arrangement.spacedBy(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        ColorCard()
        NumbersCard()
        ModifiersCard()
        MoreModifiersCard()
        ListsAndLayoutCard()
        ScrollAndPaddingCard()
    }
}

@Composable
private fun ColorCard() {
    val p = LocalPalette.current
    var color by remember { mutableStateOf(Color(0xFFB9C78F)) }
    val swatches = listOf(Color(0xFFB9C78F), Color(0xFF8FC4A0), Color(0xFF3D4D42), Color(0xFFF1EFE0), Color(0xFF1B2128))
    Section("opacity() and isDark()", "Color", Modifier.widthIn(max = 420.dp).fillMaxWidth()) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            swatches.forEach { c ->
                Box(
                    Modifier.size(44.dp).clip(RoundedCornerShape(50)).background(c)
                        .then(if (c == color) Modifier.padding(0.dp) else Modifier)
                        .bounceClick { color = c }
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(width = 110.dp, height = 64.dp).clip(RoundedCornerShape(14.dp)).background(color), contentAlignment = Alignment.Center) {
                Text("Aa", color = color.onColor(), fontWeight = FontWeight.ExtraBold)
            }
            Box(Modifier.size(width = 110.dp, height = 64.dp).clip(RoundedCornerShape(14.dp)).background(color.opacity(enabled = false, opacity = 0.5f)), contentAlignment = Alignment.Center) {
                Text("disabled", color = p.fg, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("isDark() = ${color.isDark()}", color = p.mint, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                Text("isLightColor() = ${color.isLightColor()}", color = p.mint, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                Text("contrast vs white = ${((color.calculateContrastFor(Color.White) * 100).toInt() / 100.0)}", color = p.mint, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
            }
        }
        CodeBlock(
            """val c = Color(0x${color.toArgbHex()})
c.opacity(enabled = false, opacity = 0.5f)
if (c.isDark()) Color.White else Color.Black
c.onColor()                 // Black or White for readable text
c.isLightColor()
c.calculateContrastFor(Color.White)
Icon(painter, null, tint = c)   // or Image(colorFilter = c.toColorFilter())"""
        )
    }
}

private fun Color.toArgbHex(): String {
    fun Float.h() = (this * 255f + 0.5f).toInt().toString(16).padStart(2, '0').uppercase()
    return alpha.h() + red.h() + green.h() + blue.h()
}

@Composable
private fun NumbersCard() {
    val p = LocalPalette.current
    var n by remember { mutableStateOf(205_000) }
    Section("milliSecondsToTimer() and thousandsFormat()", "Numbers", Modifier.widthIn(max = 420.dp).fillMaxWidth()) {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(950, 12_500, 205_000, 3_725_000).forEach { v -> Pill(v.toString(), selected = v == n, onClick = { n = v }, mono = true) }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Result("milliSecondsToTimer", n.milliSecondsToTimer(), Modifier.weight(1f))
            Result("thousandsFormat", n.thousandsFormat(), Modifier.weight(1f))
        }
        CodeBlock("$n.milliSecondsToTimer()  // \"${n.milliSecondsToTimer()}\"\n$n.thousandsFormat()        // \"${n.thousandsFormat()}\"")
    }
}

@Composable
private fun Result(label: String, value: String, modifier: Modifier) {
    val p = LocalPalette.current
    Column(modifier.clip(RoundedCornerShape(14.dp)).background(p.stage).padding(16.dp)) {
        Text(label, color = p.muted, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Text(value, color = p.fg, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
    }
}

@Composable
private fun ModifiersCard() {
    val p = LocalPalette.current
    var shaking by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(false) }
    var presses by remember { mutableStateOf(0) }
    val transition = rememberInfiniteTransition()
    Section("shake(), bounceClick() and conditional()", "Modifiers", Modifier.widthIn(max = 420.dp).fillMaxWidth()) {
        Column(
            Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(14.dp)).background(p.stage),
            verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier.shake(enabled = shaking, infiniteTransition = transition).clip(RoundedCornerShape(12.dp))
                    .background(p.accent).padding(horizontal = 20.dp, vertical = 12.dp)
            ) { Text("Wrong password", color = p.onAccent, fontWeight = FontWeight.Bold) }
            Row(Modifier.padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Pill(if (shaking) "Stop shake" else "Shake", selected = shaking, onClick = { shaking = !shaking })
                Box(
                    Modifier.height(40.dp).clip(RoundedCornerShape(999.dp)).background(p.mint)
                        .conditional(selected) { Modifier.background(p.accent) }
                        .bounceClick { presses++; selected = !selected }.padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) { Text("Bounce · $presses", color = Color(0xFF1B2128), fontWeight = FontWeight.Bold, fontSize = 14.sp) }
            }
        }
        CodeBlock(
            """val transition = rememberInfiniteTransition()
Modifier.shake(enabled = error, infiniteTransition = transition)

Modifier
    .conditional(isSelected) { Modifier.background(accent) }
    .bounceClick { save() }"""
        )
    }
}

@Composable
private fun MoreModifiersCard() {
    val p = LocalPalette.current
    var plain by remember { mutableStateOf(0) }
    var ripple by remember { mutableStateOf(0) }
    var tint by remember { mutableStateOf(p.accent) }
    var badge by remember { mutableStateOf<String?>(null) }
    Section("dashedBorder(), noRippleClickable(), click(), whenNotNull()", "Modifiers", Modifier.widthIn(max = 420.dp).fillMaxWidth()) {
        Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(p.stage).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                Modifier.fillMaxWidth().height(64.dp).dashedBorder(width = 2.dp, color = p.accent, shape = 14.cornerRadius(), on = 8.dp, off = 6.dp),
                contentAlignment = Alignment.Center
            ) { Text("dashedBorder(2.dp, accent, on = 8.dp, off = 6.dp)", color = p.fg, fontSize = 12.sp) }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.clip(10.cornerRadius()).background(p.soft).noRippleClickable { plain++ }.padding(horizontal = 14.dp, vertical = 10.dp)) { Text("noRipple · $plain", color = p.softFg, fontWeight = FontWeight.Bold, fontSize = 13.sp) }
                Box(Modifier.clip(10.cornerRadius()).background(tint).click { ripple++; tint = Color.random() }.padding(horizontal = 14.dp, vertical = 10.dp)) { Text("click · $ripple", color = if (tint.isDark()) Color.White else Color(0xFF1B2128), fontWeight = FontWeight.Bold, fontSize = 13.sp) }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Pill(if (badge == null) "Set badge" else "Clear badge", badge != null, { badge = if (badge == null) "NEW" else null })
                Box(Modifier.size(width = 80.dp, height = 40.dp).clip(10.cornerRadius()).background(p.panel).whenNotNull(badge) { b -> Modifier.border(BorderStroke(2.dp, p.mint)).padding(2.dp) }, contentAlignment = Alignment.Center) {
                    Text(badge ?: "none", color = p.fg, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        CodeBlock(
            """Modifier.dashedBorder(width = 2.dp, color = accent, shape = 14.cornerRadius(), on = 8.dp, off = 6.dp)

Modifier.noRippleClickable { count++ }
Modifier.click { Color.random() }

Modifier.whenNotNull(badge) { b -> Modifier.border(2.dp, mint) }"""
        )
    }
}

@Composable
private fun ListsAndLayoutCard() {
    val p = LocalPalette.current
    var header by remember { mutableStateOf(true) }
    Section("itemIf(), Int.Height(), Color.orTransparent()", "Lists & layout", Modifier.widthIn(max = 420.dp).fillMaxWidth()) {
        Pill(if (header) "Hide header item" else "Show header item", header, { header = !header }, height = 44.dp)
        Column(Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(14.dp)).background(p.stage)) {
            LazyColumn(Modifier.fillMaxWidth().padding(10.dp)) {
                itemIf(header) { Tile2("Header (itemIf)", p.mint) }
                items(5) { i -> Tile2("Item ${i + 1}", p.accent); 6.Height() }
            }
        }
        val missing: Color? = null
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(40.dp).clip(10.cornerRadius()).background(missing.orTransparent(p.soft))) {}
            Text("null.orTransparent(soft)", color = p.muted, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
        }
        CodeBlock(
            """LazyColumn {
    itemIf(showHeader) { Header() }
    items(list) { Row(it); 6.Height() }
}

val tint: Color? = null
tint.orTransparent(defaultValue = soft)"""
        )
    }
}

@Composable
private fun Tile2(label: String, color: Color) {
    val p = LocalPalette.current
    Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(color).padding(10.dp), contentAlignment = Alignment.Center) {
        Text(label, color = p.onAccent, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}

@Composable
private fun ScrollAndPaddingCard() {
    val p = LocalPalette.current
    val listState = androidx.compose.foundation.lazy.rememberLazyListState()
    val scope = androidx.compose.runtime.rememberCoroutineScope()
    var locked by remember { mutableStateOf(false) }
    val padded = androidx.compose.foundation.layout.PaddingValues(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp).withHorizontalPadding(12.dp, androidx.compose.ui.unit.LayoutDirection.Ltr)
    Section("graphicsCollapse(), disableScrolling(), withHorizontalPadding(), toColorFilter()", "Scroll & padding", Modifier.widthIn(max = 420.dp).fillMaxWidth()) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Pill(if (locked) "Enable scrolling" else "Disable scrolling", locked, {
                locked = !locked
                if (locked) listState.disableScrolling(scope) else listState.enableScrolling(scope)
            })
        }
        Box(Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(14.dp)).background(p.stage)) {
            LazyColumn(state = listState, modifier = Modifier.fillMaxSize().padding(10.dp)) {
                item { Box(Modifier.fillMaxWidth().height(54.dp).graphicsCollapse(listState).clip(RoundedCornerShape(10.dp)).background(p.mint), contentAlignment = Alignment.Center) { Text("graphicsCollapse header", color = p.onAccent, fontWeight = FontWeight.Bold, fontSize = 13.sp) } }
                items(10) { i -> Box(Modifier.fillMaxWidth().padding(top = 6.dp).clip(RoundedCornerShape(10.dp)).background(p.accent).padding(10.dp), contentAlignment = Alignment.Center) { Text("Item ${i + 1}", color = p.onAccent, fontSize = 13.sp, fontWeight = FontWeight.Bold) } }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            androidx.compose.foundation.Image(
                androidx.compose.ui.graphics.vector.rememberVectorPainter(androidx.compose.material.icons.Icons.Default.Star), contentDescription = null,
                colorFilter = p.accent.toColorFilter(), modifier = Modifier.size(32.dp)
            )
            4.Width()
            Text("start = ${padded.calculateStartPadding(androidx.compose.ui.unit.LayoutDirection.Ltr)}, end = ${padded.calculateEndPadding(androidx.compose.ui.unit.LayoutDirection.Ltr)}", color = p.muted, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
        }
        CodeBlock(
            """Modifier.graphicsCollapse(listState)      // parallax while the list scrolls

listState.disableScrolling(scope)           // lock / unlock the list
listState.enableScrolling(scope)

PaddingValues(8.dp).withHorizontalPadding(12.dp, LayoutDirection.Ltr)
Image(painter, null, colorFilter = accent.toColorFilter())
4.Width()                                   // Spacer(Modifier.width(4.dp))"""
        )
    }
}
