package com.jeluchu.jchucomponents.ui.composables.preferences

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ktx.colors.opacity

/** Compatibility wrapper for [JchuPreferenceItem]. */
@Deprecated(
    message = "Use JchuPreferenceItem with typed leadingContent",
    replaceWith = ReplaceWith("JchuPreferenceItem(title = title, onClick = onClick)")
)
@Composable
fun PreferenceItem(
    title: String,
    description: String? = null,
    icon: Any? = null,
    enabled: Boolean = true,
    containerColor: Color = Color.White,
    contentColor: Color = Color.DarkGray,
    onLongClickLabel: String? = null,
    onLongClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    titleStyle: TextStyle = MaterialTheme.typography.titleLarge,
    descriptionStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    onClick: () -> Unit = {}
) {
    val leadingContent: (@Composable () -> Unit)? = when (icon) {
        is ImageVector -> ({
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor.opacity(enabled),
                modifier = Modifier.padding(start = 8.dp).size(30.dp)
            )
        })

        is Painter -> ({
            Icon(
                painter = icon,
                contentDescription = null,
                tint = contentColor.opacity(enabled),
                modifier = Modifier.padding(start = 8.dp).size(30.dp)
            )
        })

        is Int -> ({
            CircularProgressIndicator(
                color = contentColor,
                modifier = Modifier.padding(start = 8.dp).size(30.dp).padding(2.dp)
            )
        })

        else -> null
    }

    JchuPreferenceItem(
        title = title,
        description = description,
        enabled = enabled,
        containerColor = containerColor,
        contentColor = contentColor,
        onLongClickLabel = onLongClickLabel,
        onLongClick = onLongClick,
        onClickLabel = onClickLabel,
        titleStyle = titleStyle,
        descriptionStyle = descriptionStyle,
        leadingContent = leadingContent,
        onClick = onClick
    )
}
