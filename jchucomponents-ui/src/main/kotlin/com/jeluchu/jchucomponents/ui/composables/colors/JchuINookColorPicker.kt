package com.jeluchu.jchucomponents.ui.composables.colors

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ui.R

@Immutable
data class JchuINookColorPickerColors(
    val containerColor: Color,
    val titleColor: Color,
    val paletteColor: Color,
    val descriptionColor: Color,
    val darkSelectionIndicatorColor: Color,
    val lightSelectionIndicatorColor: Color
) {
    companion object {
        fun default(darkTheme: Boolean) = JchuINookColorPickerColors(
            containerColor = if (darkTheme) Color.White else Color(0xFFA0816C),
            titleColor = if (darkTheme) Color(0xFFB3B3B3) else Color(0xFFA0816C),
            paletteColor = if (darkTheme) Color(0xFF757575) else Color(0xFFFEF8E4),
            descriptionColor = if (darkTheme) Color(0xFF757575) else Color(0xFFA0816C),
            darkSelectionIndicatorColor = if (darkTheme) Color(0xFFFEF8E4) else Color.White,
            lightSelectionIndicatorColor = if (darkTheme) Color(0xFF757575) else Color(0xFFA29574)
        )
    }
}

/** Faithful presentation port of iNook's Android color settings card. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun JchuINookColorPicker(
    title: String,
    description: String,
    colors: List<String?>,
    selectedColor: String?,
    darkTheme: Boolean,
    colorResolver: (String?) -> Color,
    onColorSelected: (String?) -> Unit,
    modifier: Modifier = Modifier,
    pickerColors: JchuINookColorPickerColors = JchuINookColorPickerColors.default(darkTheme)
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(pickerColors.containerColor.copy(alpha = .08f))
            .padding(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = pickerColors.titleColor,
            modifier = Modifier.padding(vertical = 10.dp)
        )
        FlowRow(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .background(
                    color = pickerColors.paletteColor.copy(alpha = if (darkTheme) .2f else .6f),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(vertical = 10.dp, horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            colors.distinct().forEach { color ->
                JchuINookColorItem(
                    color = colorResolver(color),
                    selected = color == selectedColor,
                    darkSelectionIndicatorColor = pickerColors.darkSelectionIndicatorColor,
                    lightSelectionIndicatorColor = pickerColors.lightSelectionIndicatorColor,
                    onClick = { onColorSelected(color) }
                )
            }
        }
        Text(
            text = description,
            color = pickerColors.descriptionColor,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 20.dp)
        )
    }
}

@Composable
private fun JchuINookColorItem(
    selected: Boolean,
    color: Color,
    darkSelectionIndicatorColor: Color,
    lightSelectionIndicatorColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .requiredSize(40.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .background(color)
            .border(1.dp, Color.Transparent, CircleShape)
    ) {
        if (selected) {
            Icon(
                painter = painterResource(R.drawable.jchu_ic_deco_check_option),
                contentDescription = null,
                tint = if (color.luminance() < .5f) {
                    darkSelectionIndicatorColor
                } else {
                    lightSelectionIndicatorColor
                },
                modifier = Modifier.size(30.dp).align(Alignment.Center)
            )
        }
    }
}
