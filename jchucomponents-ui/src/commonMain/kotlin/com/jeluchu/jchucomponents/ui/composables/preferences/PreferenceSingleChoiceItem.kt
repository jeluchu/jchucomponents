package com.jeluchu.jchucomponents.ui.composables.preferences

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeluchu.jchucomponents.ui.extensions.modifier.cornerRadius

/** Compatibility wrapper for [JchuPreferenceChoice]. */
@Deprecated(
    message = "Use JchuPreferenceChoice",
    replaceWith = ReplaceWith(
        "JchuPreferenceChoice(title = text, selected = selected, onClick = onClick, modifier = modifier)"
    )
)
@Composable
fun PreferenceSingleChoiceItem(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    shape: Shape = 15.cornerRadius(),
    style: TextStyle = MaterialTheme.typography.titleLarge,
    colors: PreferenceChoiceColors = PreferenceChoiceColors(),
    contentPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 18.dp),
    onClick: () -> Unit
) = JchuPreferenceChoice(
    title = text,
    selected = selected,
    onClick = onClick,
    modifier = modifier,
    shape = shape,
    containerColor = colors.containerColor,
    contentColor = colors.contentColor,
    radioColors = RadioButtonDefaults.colors(
        selectedColor = colors.selectedRadioColor,
        unselectedColor = colors.unselectedRadioColor
    ),
    titleStyle = style.copy(fontSize = 16.sp),
    contentPadding = contentPadding
)

@Immutable
class PreferenceChoiceColors(
    val selectedRadioColor: Color = Color.DarkGray,
    val unselectedRadioColor: Color = Color.DarkGray,
    val containerColor: Color = Color.White,
    val contentColor: Color = Color.DarkGray
)
