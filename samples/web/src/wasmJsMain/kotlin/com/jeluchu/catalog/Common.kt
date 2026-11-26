package com.jeluchu.catalog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val LocalPalette = compositionLocalOf { DarkSage }

@Composable
fun Section(title: String, kicker: String, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val p = LocalPalette.current
    Column(
        modifier.clip(RoundedCornerShape(24.dp)).background(p.panel).border(BorderStroke(1.dp, p.line), RoundedCornerShape(24.dp)).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(kicker.uppercase(), color = p.mint, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
            Text(title, color = p.fg, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
        }
        content()
    }
}

@Composable
fun CodeBlock(code: String, modifier: Modifier = Modifier) {
    val p = LocalPalette.current
    Box(modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(p.codeBg).horizontalScroll(rememberScrollState()).padding(16.dp)) {
        Text(code, color = Color(0xFFE8ECD8), fontFamily = FontFamily.Monospace, fontSize = 13.sp, lineHeight = 22.sp, softWrap = false)
    }
}

@Composable
fun Pill(label: String, selected: Boolean, onClick: () -> Unit, mono: Boolean = false, height: Dp = 40.dp) {
    val p = LocalPalette.current
    Box(
        Modifier.height(height).clip(RoundedCornerShape(999.dp))
            .background(if (selected) p.accent else Color.Transparent)
            .border(BorderStroke(1.dp, if (selected) p.accent else p.line), RoundedCornerShape(999.dp))
            .clickable(onClick = onClick).padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            label, color = if (selected) p.onAccent else p.fg, fontWeight = FontWeight.Bold, fontSize = 14.sp,
            fontFamily = if (mono) FontFamily.Monospace else FontFamily.Default
        )
    }
}
