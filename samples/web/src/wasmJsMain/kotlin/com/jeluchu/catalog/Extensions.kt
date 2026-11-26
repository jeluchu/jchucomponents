package com.jeluchu.catalog

import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.jeluchu.jchucomponents.ui.extensions.color.isDark
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
                Text("Aa", color = if (color.isDark()) Color.White else Color(0xFF1B2128), fontWeight = FontWeight.ExtraBold)
            }
            Box(Modifier.size(width = 110.dp, height = 64.dp).clip(RoundedCornerShape(14.dp)).background(color.opacity(enabled = false, opacity = 0.5f)), contentAlignment = Alignment.Center) {
                Text("disabled", color = p.fg, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            Text("isDark() = ${color.isDark()}", color = p.mint, fontFamily = FontFamily.Monospace, fontSize = 13.sp)
        }
        CodeBlock(
            """val c = Color(0x${color.toArgbHex()})
c.opacity(enabled = false, opacity = 0.5f)
if (c.isDark()) Color.White else Color.Black"""
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
