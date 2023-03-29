package com.jeluchu.jchucomponents.ui.composables.preferences

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PreferencesHintCard(
    title: String = "Title ".repeat(2),
    description: String? = "Description text ".repeat(3),
    style: TextStyle = MaterialTheme.typography.titleLarge,
    icon: ImageVector? = Icons.Outlined.Translate,
    isDarkTheme: Boolean = false,
    backgroundColor: Color = MaterialTheme.colorScheme.run { if (isDarkTheme) onPrimaryContainer else secondaryContainer },
    contentColor: Color = MaterialTheme.colorScheme.run { if (isDarkTheme) surface else onSecondaryContainer },
    onClick: () -> Unit = {},
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp)
        .clip(MaterialTheme.shapes.extraLarge)
        .background(backgroundColor)
        .clickable { onClick() }
        .padding(horizontal = 12.dp, vertical = 16.dp),
    verticalAlignment = Alignment.CenterVertically,
) {
    icon?.let {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 8.dp, end = 16.dp)
                .size(24.dp),
            tint = contentColor
        )
    }
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(start = if (icon == null) 12.dp else 0.dp, end = 12.dp)
    ) {
        Text(
            text = title,
            maxLines = 1,
            style = style.copy(fontSize = 17.sp),
            color = contentColor
        )
        if (description != null) Text(
            text = description,
            color = contentColor.copy(.6f),
            maxLines = 2,
            fontSize = 13.sp,
            overflow = TextOverflow.Ellipsis,
            style = style
        )
    }
}

@Preview
@Composable
fun PreferencesHintCardPreview() = PreferencesHintCard()