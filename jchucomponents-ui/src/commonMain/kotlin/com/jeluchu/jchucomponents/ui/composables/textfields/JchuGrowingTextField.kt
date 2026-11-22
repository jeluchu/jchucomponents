package com.jeluchu.jchucomponents.ui.composables.textfields

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun JchuGrowingTextField(
    value: String,
    onValueChange: (String) -> Unit,
    defaults: GrowingTextFieldDefaults,
    modifier: Modifier = Modifier
) {
    require(defaults.minLines > 0)
    require(defaults.maxLines >= defaults.minLines)
    require(defaults.maxCharacters == null || defaults.maxCharacters >= 0)
    val colors = defaults.colors ?: GrowingTextFieldColorsDefaults.colors()

    Column(
        modifier =
            modifier.then(
                if (defaults.animateContentChanges) {
                    Modifier.animateContentSize(
                        animationSpec =
                            spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessMediumLow
                            )
                    )
                } else {
                    Modifier
                }
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            defaults.icon?.let { icon ->
                Icon(
                    imageVector = icon,
                    contentDescription = defaults.iconContentDescription,
                    tint = colors.labelColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = defaults.label,
                color = colors.labelColor,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        OutlinedTextField(
            value = value,
            onValueChange = { updatedValue ->
                onValueChange(
                    defaults.maxCharacters?.let(updatedValue::take) ?: updatedValue
                )
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = defaults.placeholder,
                    color = colors.placeholderColor
                )
            },
            textStyle = defaults.textStyle,
            minLines = defaults.minLines,
            maxLines = defaults.maxLines,
            keyboardOptions =
                KeyboardOptions(
                    capitalization = defaults.capitalization,
                    keyboardType = defaults.keyboardType
                ),
            shape = defaults.shape,
            colors =
                TextFieldDefaults.colors(
                    focusedTextColor = colors.contentColor,
                    unfocusedTextColor = colors.contentColor,
                    focusedContainerColor = colors.containerColor,
                    unfocusedContainerColor = colors.containerColor,
                    focusedIndicatorColor = colors.focusedIndicatorColor,
                    unfocusedIndicatorColor = colors.unfocusedIndicatorColor,
                    cursorColor = colors.cursorColor
                )
        )

        defaults.maxCharacters?.let { maximum ->
            AnimatedContent(
                targetState = value.length.coerceAtMost(maximum),
                modifier = Modifier.fillMaxWidth(),
                transitionSpec = {
                    if (defaults.animateContentChanges) {
                        (
                            slideInVertically { height -> height / 2 } + fadeIn()
                        ) togetherWith (
                            slideOutVertically { height -> -height / 2 } + fadeOut()
                        )
                    } else {
                        fadeIn(animationSpec = snap()) togetherWith
                            fadeOut(animationSpec = snap())
                    }
                },
                label = "GrowingTextFieldCounter"
            ) { count ->
                Text(
                    text = "$count / $maximum",
                    modifier = Modifier.fillMaxWidth(),
                    color = colors.counterColor,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Immutable
data class GrowingTextFieldDefaults(
    val label: String,
    val placeholder: String = "",
    val icon: ImageVector? = null,
    val iconContentDescription: String? = null,
    val minLines: Int = 3,
    val maxLines: Int = Int.MAX_VALUE,
    val maxCharacters: Int? = null,
    val keyboardType: KeyboardType = KeyboardType.Text,
    val capitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
    val textStyle: TextStyle = TextStyle.Default,
    val shape: Shape = RoundedCornerShape(16.dp),
    val colors: GrowingTextFieldColors? = null,
    val animateContentChanges: Boolean = true
)

@Immutable
data class GrowingTextFieldColors(
    val containerColor: Color,
    val contentColor: Color,
    val labelColor: Color,
    val placeholderColor: Color,
    val counterColor: Color,
    val cursorColor: Color,
    val focusedIndicatorColor: Color,
    val unfocusedIndicatorColor: Color
)

object GrowingTextFieldColorsDefaults {
    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
        contentColor: Color = MaterialTheme.colorScheme.onSurface,
        labelColor: Color = MaterialTheme.colorScheme.onSurface,
        placeholderColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        counterColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        cursorColor: Color = MaterialTheme.colorScheme.primary,
        focusedIndicatorColor: Color = MaterialTheme.colorScheme.primary,
        unfocusedIndicatorColor: Color = MaterialTheme.colorScheme.outline
    ) = GrowingTextFieldColors(
        containerColor = containerColor,
        contentColor = contentColor,
        labelColor = labelColor,
        placeholderColor = placeholderColor,
        counterColor = counterColor,
        cursorColor = cursorColor,
        focusedIndicatorColor = focusedIndicatorColor,
        unfocusedIndicatorColor = unfocusedIndicatorColor
    )
}

@Preview(showBackground = true)
@Composable
private fun JchuGrowingTextFieldPreview() {
    JchuGrowingTextField(
        value = "A reusable growing text field.",
        onValueChange = {},
        defaults =
            GrowingTextFieldDefaults(
                label = "Notes",
                placeholder = "Write something",
                icon = Icons.Default.Edit,
                maxCharacters = 120
            ),
        modifier = Modifier.padding(16.dp)
    )
}
