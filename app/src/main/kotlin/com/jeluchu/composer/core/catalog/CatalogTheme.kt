package com.jeluchu.composer.core.catalog

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.jeluchu.composer.core.ui.theme.darkGreen as catalogAccent
import com.jeluchu.composer.core.ui.theme.darkness as catalogContent
import com.jeluchu.composer.core.ui.theme.milky as catalogContentInverse
import com.jeluchu.composer.core.ui.theme.primary as catalogPrimary
import com.jeluchu.composer.core.ui.theme.secondary as catalogBackground
import com.jeluchu.jchucomponents.ui.theme.LocalShapes
import com.jeluchu.jchucomponents.ui.theme.LocalSpacing
import com.jeluchu.jchucomponents.ui.theme.Shapes
import com.jeluchu.jchucomponents.ui.theme.Spacing

object JchuCatalogTheme {
    val colors: CatalogColors
        @Composable
        @ReadOnlyComposable
        get() = LocalCatalogColors.current

    val spacing: Spacing
        @Composable
        @ReadOnlyComposable
        get() = LocalSpacing.current

    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = LocalShapes.current

    val typography: CatalogTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalCatalogTypography.current

    val motion: CatalogMotion
        @Composable
        @ReadOnlyComposable
        get() = LocalCatalogMotion.current
}

@Deprecated(
    message = "Use JchuCatalogTheme.",
    replaceWith = ReplaceWith("JchuCatalogTheme")
)
object CatalogTheme {
    val colors: CatalogColors
        @Composable
        @ReadOnlyComposable
        get() = JchuCatalogTheme.colors

    val spacing: Spacing
        @Composable
        @ReadOnlyComposable
        get() = JchuCatalogTheme.spacing

    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = JchuCatalogTheme.shapes

    val typography: CatalogTypography
        @Composable
        @ReadOnlyComposable
        get() = JchuCatalogTheme.typography

    val motion: CatalogMotion
        @Composable
        @ReadOnlyComposable
        get() = JchuCatalogTheme.motion
}

@Stable
class CatalogColors(
    val background: Color = catalogBackground,
    val surface: Color = catalogBackground.copy(.4f),
    val primary: Color = catalogPrimary,
    val content: Color = catalogContent,
    val contentInverse: Color = catalogContentInverse,
    val accent: Color = catalogAccent,
    val error: Color = Color(0xFFB3261E),
) {
    companion object {
        fun light() = CatalogColors()

        fun dark() = CatalogColors(
            background = Color(0xFF101411),
            surface = Color(0xFF1B211D),
            primary = Color(0xFFD7F2DF),
            content = Color(0xFFF2F7F0),
            contentInverse = Color(0xFF101411),
            accent = Color(0xFF8FD6A4),
            error = Color(0xFFFFB4AB),
        )

        fun highContrast() = CatalogColors(
            background = Color.White,
            surface = Color.White,
            primary = Color.Black,
            content = Color.Black,
            contentInverse = Color.White,
            accent = Color(0xFF005F24),
            error = Color(0xFF8C1D18),
        )
    }
}

@Stable
class CatalogTypography(
    val title: TextStyle,
    val section: TextStyle,
    val body: TextStyle,
    val label: TextStyle,
)

@Stable
class CatalogMotion(
    val durationShortMillis: Int = 150,
    val durationMediumMillis: Int = 250,
    val durationLongMillis: Int = 400,
)

val LocalCatalogColors = staticCompositionLocalOf { CatalogColors() }
val LocalCatalogMotion = staticCompositionLocalOf { CatalogMotion() }
val LocalCatalogTypography = staticCompositionLocalOf {
    CatalogTypography(
        title = TextStyle.Default,
        section = TextStyle.Default,
        body = TextStyle.Default,
        label = TextStyle.Default,
    )
}

@Composable
fun rememberCatalogTypography() = CatalogTypography(
    title = MaterialTheme.typography.titleLarge,
    section = MaterialTheme.typography.titleMedium,
    body = MaterialTheme.typography.bodyMedium,
    label = MaterialTheme.typography.labelSmall,
)

@Composable
fun ProvideJchuCatalogTheme(
    colors: CatalogColors = JchuCatalogTheme.colors,
    spacing: Spacing = LocalSpacing.current,
    shapes: Shapes = LocalShapes.current,
    typography: CatalogTypography = rememberCatalogTypography(),
    motion: CatalogMotion = JchuCatalogTheme.motion,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalCatalogColors provides colors,
        LocalSpacing provides spacing,
        LocalShapes provides shapes,
        LocalCatalogTypography provides typography,
        LocalCatalogMotion provides motion,
        content = content
    )
}
