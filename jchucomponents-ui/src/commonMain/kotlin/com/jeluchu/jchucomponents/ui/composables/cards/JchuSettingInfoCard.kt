package com.jeluchu.jchucomponents.ui.composables.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A settings-card container with optional click behavior and caller-owned content.
 *
 * Supply the leading icon, text and trailing action through [content]. Keeping those slots in the
 * caller lets each app retain its own typography, resource handling and icon style.
 */
@Composable
fun JchuSettingInfoCard(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(15.dp),
    colors: JchuSettingInfoCardColors = JchuSettingInfoCardDefaults.colors(),
    contentPadding: PaddingValues = PaddingValues(15.dp),
    contentSpacing: Dp = 15.dp,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable RowScope.() -> Unit
) {
    val containerColor = if (enabled) colors.containerColor else colors.disabledContainerColor
    val contentColor = if (enabled) colors.contentColor else colors.disabledContentColor
    val clickAction = onClick
    val cardModifier = if (clickAction == null) {
        modifier
    } else {
        modifier
            .clip(shape)
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = clickAction
            )
    }

    Surface(
        modifier = cardModifier,
        shape = shape,
        color = containerColor,
        contentColor = contentColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding),
            horizontalArrangement = Arrangement.spacedBy(contentSpacing),
            verticalAlignment = verticalAlignment,
            content = content
        )
    }
}

/** Theme-aware enabled and disabled colors for [JchuSettingInfoCard]. */
@Immutable
data class JchuSettingInfoCardColors(
    val containerColor: Color,
    val contentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color
)

/** Default values for [JchuSettingInfoCard]. */
object JchuSettingInfoCardDefaults {
    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.surface,
        contentColor: Color = MaterialTheme.colorScheme.onSurface,
        disabledContainerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
        disabledContentColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
    ) = JchuSettingInfoCardColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor
    )
}
