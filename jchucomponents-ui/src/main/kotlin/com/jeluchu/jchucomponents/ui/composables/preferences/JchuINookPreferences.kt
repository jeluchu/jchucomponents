package com.jeluchu.jchucomponents.ui.composables.preferences

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeluchu.jchucomponents.ui.R

/** Faithful port of iNook's plain preference row. */
@Composable
fun JchuPlainPreference(
    title: String,
    navigationContentDescription: String,
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    colors: JchuPlainPreferenceColors = JchuPlainPreferenceColors(),
    subtitle: String? = null,
    onClick: () -> Unit
) = Row(
    modifier =
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .background(colors.containerColor)
            .padding(vertical = 10.dp, horizontal = 15.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
) {
    icon?.let {
        Icon(
            painter = painterResource(it),
            contentDescription = "",
            tint = colors.contentColor
        )
    }

    Column(
        modifier =
            if (subtitle != null) {
                Modifier.weight(2f).padding(16.dp)
            } else {
                Modifier.weight(2f).padding(horizontal = 16.dp)
            }
    ) {
        Text(
            text = title,
            color = colors.contentColor,
            style = MaterialTheme.typography.bodySmall
        )
        if (subtitle != null) {
            Text(
                text = subtitle,
                color = colors.contentColor.copy(alpha = .2f),
                fontSize = 13.sp
            )
        }
    }

    Icon(
        tint = colors.contentColor,
        contentDescription = navigationContentDescription,
        imageVector = ImageVector.vectorResource(R.drawable.jchu_ic_deco_filled_arrow_right)
    )
}

@Immutable
class JchuPlainPreferenceColors(
    val contentColor: Color = Color.DarkGray,
    val containerColor: Color = Color.White.copy(alpha = .2f)
)

/** Faithful vector-icon port of iNook's switch preference row. */
@Composable
fun JchuSwitchPreference(
    title: String,
    value: Boolean,
    darkTheme: Boolean,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    imageVector: ImageVector? = null,
    onValueChange: (Boolean) -> Unit,
    colors: JchuSwitchPreferenceColors = JchuSwitchPreferenceColors()
) = JchuSwitchPreferenceLayout(
    title = title,
    value = value,
    darkTheme = darkTheme,
    modifier = modifier,
    subtitle = subtitle,
    leadingContent =
        imageVector?.let {
            {
                Icon(
                    contentDescription = "",
                    imageVector = it,
                    tint = colors.contentColor,
                    modifier = Modifier.size(30.dp)
                )
            }
        },
    painterSubtitleColor = false,
    onValueChange = onValueChange,
    colors = colors
)

/** Faithful painter-icon port of iNook's switch preference row. */
@Composable
fun JchuSwitchPreference(
    title: String,
    value: Boolean,
    darkTheme: Boolean,
    modifier: Modifier = Modifier,
    painter: Painter? = null,
    subtitle: String? = null,
    onValueChange: (Boolean) -> Unit,
    colors: JchuSwitchPreferenceColors = JchuSwitchPreferenceColors()
) = JchuSwitchPreferenceLayout(
    title = title,
    value = value,
    darkTheme = darkTheme,
    modifier = modifier,
    subtitle = subtitle,
    leadingContent =
        painter?.let {
            {
                Image(
                    painter = it,
                    contentDescription = "",
                    modifier = Modifier.size(30.dp),
                    contentScale = ContentScale.FillWidth
                )
            }
        },
    painterSubtitleColor = true,
    onValueChange = onValueChange,
    colors = colors
)

@Composable
private fun JchuSwitchPreferenceLayout(
    title: String,
    value: Boolean,
    darkTheme: Boolean,
    modifier: Modifier,
    subtitle: String?,
    leadingContent: (@Composable () -> Unit)?,
    painterSubtitleColor: Boolean,
    onValueChange: (Boolean) -> Unit,
    colors: JchuSwitchPreferenceColors
) = Row(
    modifier =
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(colors.containerColor)
            .clickable { onValueChange(!value) }
            .padding(vertical = 10.dp, horizontal = 15.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
) {
    leadingContent?.invoke()
    Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
        Text(
            text = title,
            lineHeight = 20.sp,
            color = colors.contentColor,
            style = MaterialTheme.typography.bodyMedium
        )
        subtitle?.let {
            Text(
                text = it,
                color =
                    if (painterSubtitleColor) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        colors.contentColor.copy(alpha = .7f)
                    },
                fontSize = 13.sp,
                lineHeight = 14.sp
            )
        }
    }
    Switch(
        checked = value,
        thumbContent = {
            Icon(
                contentDescription = title,
                modifier = Modifier.size(15.dp),
                imageVector = ImageVector.vectorResource(R.drawable.jchu_ic_deco_leaf_toggle)
            )
        },
        colors = jchuINookSwitchColors(darkTheme),
        onCheckedChange = { onValueChange(it) }
    )
}

@Composable
private fun jchuINookSwitchColors(darkTheme: Boolean) = SwitchDefaults.colors(
    checkedThumbColor = if (darkTheme) Color(0xFFE0F4B9) else Color(0xFF6A886C),
    checkedBorderColor = if (darkTheme) Color(0xFF757575) else Color(0xFF6A886C),
    uncheckedBorderColor = if (darkTheme) Color(0xFF757575) else Color(0xFFB04D4E),
    uncheckedThumbColor = if (darkTheme) Color(0xFFFF7878) else Color(0xFFB04D4E),
    checkedIconColor = if (darkTheme) Color(0xE6444444) else Color(0xE6FEF8E4),
    checkedTrackColor = if (darkTheme) Color(0x80444444) else Color(0x806A886C),
    uncheckedIconColor = if (darkTheme) Color(0xE6444444) else Color(0xE6FEF8E4),
    uncheckedTrackColor = if (darkTheme) Color(0x80444444) else Color(0x80FF7878)
)

@Immutable
class JchuSwitchPreferenceColors(
    val contentColor: Color = Color.DarkGray,
    val containerColor: Color = Color.White.copy(alpha = .2f)
)
