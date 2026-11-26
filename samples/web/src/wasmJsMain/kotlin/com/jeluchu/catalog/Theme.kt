package com.jeluchu.catalog

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/** Sage palette taken from the project avatar. */
class CatalogPalette(
    val dark: Boolean,
    val bg: Color,
    val panel: Color,
    val stage: Color,
    val line: Color,
    val fg: Color,
    val muted: Color,
    val accent: Color,
    val onAccent: Color,
    val mint: Color,
    val soft: Color,
    val softFg: Color,
    val codeBg: Color
) {
    val scheme: ColorScheme =
        if (dark) {
            darkColorScheme(
                primary = accent, onPrimary = onAccent, secondary = mint, background = bg,
                surface = panel, onSurface = fg, onBackground = fg, surfaceVariant = stage, outline = line
            )
        } else {
            lightColorScheme(
                primary = accent, onPrimary = onAccent, secondary = mint, background = bg,
                surface = panel, onSurface = fg, onBackground = fg, surfaceVariant = stage, outline = line
            )
        }
}

val DarkSage =
    CatalogPalette(
        dark = true, bg = Color(0xFF1B2128), panel = Color(0xFF232B33), stage = Color(0xFF1D252C),
        line = Color(0xFF34404A), fg = Color(0xFFEEF0E4), muted = Color(0xFFA9B3A9), accent = Color(0xFFB9C78F),
        onAccent = Color(0xFF1B2128), mint = Color(0xFF8FC4A0), soft = Color(0xFF3D4D42),
        softFg = Color(0xFFD4E3B4), codeBg = Color(0xFF141A1F)
    )

val LightSage =
    CatalogPalette(
        dark = false, bg = Color(0xFFF3F5E8), panel = Color.White, stage = Color(0xFFEEF2DC),
        line = Color(0xFFDFE4CC), fg = Color(0xFF1F2A23), muted = Color(0xFF55625A), accent = Color(0xFF587656),
        onAccent = Color.White, mint = Color(0xFF3F7A57), soft = Color(0xFFE1EBC4),
        softFg = Color(0xFF34502F), codeBg = Color(0xFF1F2A23)
    )
