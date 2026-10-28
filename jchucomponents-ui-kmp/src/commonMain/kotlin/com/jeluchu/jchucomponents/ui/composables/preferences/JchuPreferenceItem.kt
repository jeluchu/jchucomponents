package com.jeluchu.jchucomponents.ui.composables.preferences

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

/** A controlled preference row with typed composable slots for leading and trailing content. */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JchuPreferenceItem(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    titleStyle: TextStyle = MaterialTheme.typography.titleMedium,
    descriptionStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    onClickLabel: String? = null,
    onLongClickLabel: String? = null,
    onLongClick: (() -> Unit)? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null
) = Surface(
    modifier =
        modifier.combinedClickable(
            enabled = enabled,
            onClickLabel = onClickLabel,
            onLongClickLabel = onLongClickLabel,
            onLongClick = onLongClick,
            onClick = onClick
        ),
    color = containerColor,
    contentColor = contentColor
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingContent != null) {
            leadingContent()
        }
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(start = if (leadingContent == null) 0.dp else 16.dp)
                    .padding(end = if (trailingContent == null) 0.dp else 16.dp)
        ) {
            PreferenceItemTitle(
                text = title,
                enabled = enabled,
                style = titleStyle,
                color = contentColor
            )
            if (!description.isNullOrEmpty()) {
                PreferenceItemDescription(
                    text = description,
                    enabled = enabled,
                    style = descriptionStyle,
                    color = contentColor
                )
            }
        }
        trailingContent?.invoke(this)
    }
}
