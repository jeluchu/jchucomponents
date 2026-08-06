package com.jeluchu.jchucomponents.ui.composables.text

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow

/**
 * Displays text with a caller-controlled expanded state.
 *
 * The action remains visible for predictable behavior with dynamic text and initial expanded
 * states. Callers should provide localized [expandLabel] and [collapseLabel] values.
 */
@Composable
fun JchuExpandableText(
    text: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    collapsedMaxLines: Int = 3,
    expandLabel: String = "Show more",
    collapseLabel: String = "Show less",
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    require(collapsedMaxLines > 0) { "collapsedMaxLines must be greater than zero" }

    Column(modifier = modifier.animateContentSize()) {
        Text(
            text = text,
            modifier = Modifier.fillMaxWidth(),
            maxLines = if (expanded) Int.MAX_VALUE else collapsedMaxLines,
            overflow = TextOverflow.Ellipsis,
            style = style,
            color = color
        )
        TextButton(
            onClick = { onExpandedChange(!expanded) },
            modifier = Modifier.align(Alignment.End)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(if (expanded) collapseLabel else expandLabel)
                Icon(
                    imageVector = if (expanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                    contentDescription = null
                )
            }
        }
    }
}
