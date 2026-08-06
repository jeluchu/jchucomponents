package com.jeluchu.jchucomponents.ui.composables.text

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeluchu.jchucomponents.ui.R
import com.jeluchu.jchucomponents.ui.composables.images.NetworkImage
import com.jeluchu.jchucomponents.ui.extensions.modifier.noRippleClickable
import kotlin.math.roundToInt

private val jchuLegacyWhitespaceLineRegex = Regex("[\\r\\n]{2,}", setOf(RegexOption.MULTILINE))
private val jchuLegacyFlowerContent = Color(0xFF59746B)
private val jchuLegacyFlowerGradient = Color(0xFFADD1B1)
private val jchuLegacyArtichoke = Color(0xFFA29574)

@Immutable
class JchuLegacyExpandableDescriptionColors(
    val contentColor: Color = Color(0xFF8B7E6D),
    val gradientColor: Color = Color(0xFF8B7E6D),
    val containerColor: Color = Color(0xFF8B7E6D)
)

/** Literal port of the first-generation iNook expandable description. */
@Composable
fun JchuLegacyExpandableDescription(
    image: String,
    description: String?,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(10.dp),
    defaultExpandState: Boolean = false,
    title: String = "Description",
    colors: JchuLegacyExpandableDescriptionColors = JchuLegacyExpandableDescriptionColors()
) {
    Column(
        modifier = modifier
            .clip(shape)
            .background(colors.containerColor)
            .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val (expanded, onExpanded) = rememberSaveable { mutableStateOf(defaultExpandState) }
        val desc = description.takeIf { !it.isNullOrBlank() }.orEmpty()
        val trimmedDescription = remember(desc) {
            desc.replace(jchuLegacyWhitespaceLineRegex, "\n").trimEnd()
        }
        Text(
            text = title,
            color = colors.contentColor,
            style = MaterialTheme.typography.bodyLarge
        )
        JchuLegacyExpandableSummary(
            expanded = expanded,
            expandedDescription = desc,
            shrunkDescription = trimmedDescription,
            gradientColor = colors.gradientColor,
            contentColor = colors.contentColor.copy(alpha = .7f),
            modifier = Modifier.noRippleClickable { onExpanded(!expanded) }
        )
        NetworkImage(
            url = image,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(10.dp))
        )
    }
}

@Composable
fun JchuLegacyExpandableDescriptionGallery(
    description: String?,
    images: List<String>,
    modifier: Modifier = Modifier,
    defaultExpandState: Boolean = false,
    title: String = "Description"
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        val (expanded, onExpanded) = rememberSaveable { mutableStateOf(defaultExpandState) }
        val desc = description.takeIf { !it.isNullOrBlank() }.orEmpty()
        val trimmedDescription = remember(desc) {
            desc.replace(jchuLegacyWhitespaceLineRegex, "\n").trimEnd()
        }
        Text(
            text = title,
            modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 15.dp),
            color = jchuLegacyFlowerContent,
            style = MaterialTheme.typography.titleSmall
        )
        JchuLegacyExpandableSummary(
            expanded = expanded,
            expandedDescription = desc,
            shrunkDescription = trimmedDescription,
            contentColor = jchuLegacyFlowerContent.copy(alpha = .7f),
            gradientColor = jchuLegacyFlowerGradient,
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .noRippleClickable { onExpanded(!expanded) }
        )
        LazyRow(
            contentPadding = PaddingValues(15.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(images) { image ->
                NetworkImage(
                    url = image,
                    modifier = Modifier.size(100.dp).clip(RoundedCornerShape(16.dp))
                )
            }
        }
    }
}

@Composable
fun JchuLegacySimpleExpandableText(
    description: String?,
    modifier: Modifier = Modifier,
    defaultExpandState: Boolean = false
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        val (expanded, onExpanded) = rememberSaveable { mutableStateOf(defaultExpandState) }
        val desc = description.takeIf { !it.isNullOrBlank() }.orEmpty()
        val trimmedDescription = remember(desc) {
            desc.replace(jchuLegacyWhitespaceLineRegex, "\n").trimEnd()
        }
        JchuLegacyExpandableSummary(
            expanded = expanded,
            expandedDescription = desc,
            shrunkDescription = trimmedDescription,
            contentColor = jchuLegacyFlowerContent.copy(alpha = .7f),
            gradientColor = jchuLegacyFlowerGradient,
            modifier = Modifier
                .padding(10.dp)
                .noRippleClickable { onExpanded(!expanded) }
        )
    }
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun JchuLegacyExpandableSummary(
    expandedDescription: String,
    shrunkDescription: String,
    expanded: Boolean,
    contentColor: Color,
    gradientColor: Color,
    modifier: Modifier = Modifier
) {
    val animProgress by animateFloatAsState(if (expanded) 1f else 0f)
    Layout(
        modifier = modifier.clipToBounds(),
        contents = listOf(
            { Text(text = "\n\n", style = MaterialTheme.typography.bodyMedium) },
            {
                Text(
                    text = expandedDescription,
                    lineHeight = 20.sp,
                    style = MaterialTheme.typography.bodySmall,
                    color = jchuLegacyFlowerContent.copy(alpha = .7f)
                )
            },
            {
                SelectionContainer {
                    Text(
                        text = if (expanded) expandedDescription else shrunkDescription,
                        lineHeight = 20.sp,
                        color = contentColor,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 15.dp)
                    )
                }
            },
            {
                val colors = listOf(Color.Transparent, gradientColor)
                Box(
                    modifier = Modifier.background(
                        brush = Brush.linearGradient(
                            colors = listOf(jchuLegacyArtichoke, jchuLegacyArtichoke),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    if (expandedDescription.length > 200) {
                        val image = AnimatedImageVector.animatedVectorResource(
                            R.drawable.jchu_ic_deco_anim_arrow_down
                        )
                        Icon(
                            painter = rememberAnimatedVectorPainter(image, !expanded),
                            contentDescription = "",
                            tint = Color.DarkGray,
                            modifier = Modifier.background(
                                Brush.radialGradient(colors = colors.asReversed())
                            )
                        )
                    }
                }
            }
        )
    ) { (shrunk, expandedText, actual, scrim), constraints ->
        val shrunkHeight = shrunk.single().measure(constraints).height
        val expandedHeight = expandedText.single().measure(constraints).height
        val heightDelta = expandedHeight - shrunkHeight
        val scrimHeight = 24.dp.roundToPx()
        val actualPlaceable = actual.single().measure(constraints)
        val scrimPlaceable = scrim.single().measure(
            Constraints.fixed(width = constraints.maxWidth, height = scrimHeight)
        )
        val currentHeight = shrunkHeight +
            ((heightDelta + scrimHeight) * animProgress).roundToInt()
        layout(constraints.maxWidth, currentHeight) {
            actualPlaceable.place(0, 0)
            scrimPlaceable.place(0, currentHeight - scrimHeight)
        }
    }
}
