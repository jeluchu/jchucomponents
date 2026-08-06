package com.jeluchu.jchucomponents.ui.composables.text

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeluchu.jchucomponents.ui.R
import com.jeluchu.jchucomponents.ui.composables.images.NetworkImage

/** Android defaults copied from iNook's Compose implementation. */
@Immutable
data class JchuExpandableDescriptionConfig(
    val defaultExpanded: Boolean = false,
    val maxCollapsedLines: Int = 3,
    val minCharactersForExpansion: Int = 100,
    val animationDurationMs: Int = 300,
    val enableHapticFeedback: Boolean = true,
    val textAlign: TextAlign = TextAlign.Start
) {
    init {
        require(maxCollapsedLines > 0) { "maxCollapsedLines must be greater than zero" }
        require(minCharactersForExpansion >= 0) {
            "minCharactersForExpansion must not be negative"
        }
        require(animationDurationMs >= 0) { "animationDurationMs must not be negative" }
    }
}

@Immutable
data class JchuExpandableDescriptionColors(
    val contentColor: Color,
    val titleColor: Color,
    val borderColor: Color,
    val containerColor: Color,
    val gradientColor: Color,
    val iconColor: Color
) {
    companion object {
        @Composable
        fun default(
            contentColor: Color = MaterialTheme.colorScheme.onSurface,
            titleColor: Color = MaterialTheme.colorScheme.onSurface,
            containerColor: Color = MaterialTheme.colorScheme.surface,
            borderColor: Color = MaterialTheme.colorScheme.surface,
            gradientColor: Color = MaterialTheme.colorScheme.surface,
            iconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
        ) = JchuExpandableDescriptionColors(
            contentColor = contentColor,
            titleColor = titleColor,
            borderColor = borderColor,
            containerColor = containerColor,
            gradientColor = gradientColor,
            iconColor = iconColor
        )

        @Composable
        fun dark() = JchuExpandableDescriptionColors(
            contentColor = Color(0xFFE0E0E0),
            titleColor = Color.White,
            borderColor = Color.White,
            containerColor = Color(0xFF1E1E1E),
            gradientColor = Color(0xFF1E1E1E),
            iconColor = Color(0xFFBDBDBD)
        )
    }
}

private val jchuWhitespaceLineRegex = Regex("[\\r\\n]{2,}", setOf(RegexOption.MULTILINE))

private fun processJchuDescription(description: String?): String =
    description
        .takeIf { !it.isNullOrBlank() }
        .orEmpty()
        .replace(jchuWhitespaceLineRegex, "\n")
        .trimEnd()

@Composable
fun rememberJchuExpandableDescriptionState(
    defaultExpanded: Boolean = false,
    onStateChange: ((Boolean) -> Unit)? = null
): Pair<Boolean, (Boolean) -> Unit> {
    val (expanded, setExpanded) = rememberSaveable { mutableStateOf(defaultExpanded) }
    val updateExpanded: (Boolean) -> Unit = remember(onStateChange) {
        { newValue: Boolean ->
            setExpanded(newValue)
            onStateChange?.invoke(newValue)
        }
    }
    return expanded to updateExpanded
}

@Composable
fun JchuExpandableDescription(
    image: String,
    description: String?,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(10.dp),
    config: JchuExpandableDescriptionConfig = JchuExpandableDescriptionConfig(),
    title: String = "Description",
    colors: JchuExpandableDescriptionColors = JchuExpandableDescriptionColors.default(),
    onExpandedChange: ((Boolean) -> Unit)? = null
) {
    JchuExpandableDescriptionContainer(
        modifier = modifier,
        description = description,
        shape = shape,
        config = config,
        title = title,
        colors = colors,
        onExpandedChange = onExpandedChange
    ) {
        NetworkImage(
            url = image,
            contentDescription = "Description image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(10.dp))
        )
    }
}

@Composable
fun JchuExpandableDescriptionGallery(
    description: String?,
    images: List<String>,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(10.dp),
    config: JchuExpandableDescriptionConfig = JchuExpandableDescriptionConfig(),
    title: String = "Description",
    colors: JchuExpandableDescriptionColors = JchuExpandableDescriptionColors.default(),
    onExpandedChange: ((Boolean) -> Unit)? = null,
    onImageClick: ((String, Int) -> Unit)? = null
) {
    val haptic = LocalHapticFeedback.current
    val (expanded, onExpanded) = rememberJchuExpandableDescriptionState(
        config.defaultExpanded,
        onExpandedChange
    )
    val processedDescription = remember(description) { processJchuDescription(description) }

    Column(
        modifier = modifier
            .clip(shape)
            .background(colors.containerColor)
            .border(1.dp, colors.borderColor, shape),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = title,
            color = colors.titleColor,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 15.dp, start = 15.dp, end = 15.dp)
        )
        JchuAnimatedSummary(
            expanded = expanded,
            text = processedDescription,
            config = config,
            colors = colors,
            modifier = Modifier.padding(start = 15.dp, end = 15.dp),
            onToggle = {
                if (config.enableHapticFeedback) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
                onExpanded(!expanded)
            }
        )
        if (images.isNotEmpty()) {
            JchuExpandableImageGallery(images, onImageClick)
        }
    }
}

@Composable
fun JchuExpandableDescriptionAction(
    image: String,
    description: String?,
    action: String,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(10.dp),
    config: JchuExpandableDescriptionConfig = JchuExpandableDescriptionConfig(),
    title: String = "Description",
    colors: JchuExpandableDescriptionColors = JchuExpandableDescriptionColors.default(),
    onExpandedChange: ((Boolean) -> Unit)? = null,
    onActionClick: (() -> Unit)? = null
) {
    JchuExpandableDescriptionContainer(
        modifier = modifier,
        description = description,
        shape = shape,
        config = config,
        title = title,
        colors = colors,
        onExpandedChange = onExpandedChange
    ) {
        NetworkImage(
            url = image,
            contentDescription = "Description image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        onActionClick?.let { click ->
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .clickable { click() }
                    .background(colors.contentColor.copy(alpha = .1f))
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Text(
                    text = action,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = colors.contentColor
                )
                Icon(
                    painter = painterResource(R.drawable.jchu_ic_deco_filled_arrow_right),
                    tint = colors.contentColor,
                    contentDescription = ""
                )
            }
        }
    }
}

@Composable
fun JchuSimpleExpandableText(
    description: String?,
    modifier: Modifier = Modifier,
    config: JchuExpandableDescriptionConfig = JchuExpandableDescriptionConfig(),
    colors: JchuExpandableDescriptionColors = JchuExpandableDescriptionColors.default(),
    onExpandedChange: ((Boolean) -> Unit)? = null
) {
    val haptic = LocalHapticFeedback.current
    val (expanded, onExpanded) = rememberJchuExpandableDescriptionState(
        config.defaultExpanded,
        onExpandedChange
    )
    val processedDescription = remember(description) { processJchuDescription(description) }
    JchuAnimatedSummary(
        expanded = expanded,
        text = processedDescription,
        config = config,
        colors = colors,
        modifier = modifier,
        onToggle = {
            if (config.enableHapticFeedback) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
            onExpanded(!expanded)
        }
    )
}

@Composable
private fun JchuExpandableDescriptionContainer(
    description: String?,
    shape: Shape,
    config: JchuExpandableDescriptionConfig,
    title: String,
    colors: JchuExpandableDescriptionColors,
    onExpandedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val (expanded, onExpanded) = rememberJchuExpandableDescriptionState(
        config.defaultExpanded,
        onExpandedChange
    )
    val processedDescription = remember(description) { processJchuDescription(description) }
    Column(
        modifier = modifier
            .clip(shape)
            .background(colors.containerColor)
            .border(1.dp, colors.borderColor, shape)
            .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = title, color = colors.titleColor, style = MaterialTheme.typography.bodyMedium)
        JchuAnimatedSummary(
            expanded = expanded,
            text = processedDescription,
            config = config,
            colors = colors,
            onToggle = {
                if (config.enableHapticFeedback) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
                onExpanded(!expanded)
            }
        )
        content()
    }
}

@Composable
private fun JchuAnimatedSummary(
    expanded: Boolean,
    text: String,
    config: JchuExpandableDescriptionConfig,
    colors: JchuExpandableDescriptionColors,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = expanded,
        modifier = modifier,
        transitionSpec = {
            fadeIn(tween(config.animationDurationMs)) togetherWith
                fadeOut(tween(config.animationDurationMs))
        },
        label = "description_animation"
    ) { isExpanded ->
        JchuEnhancedSummary(
            expanded = isExpanded,
            text = text,
            config = config,
            colors = colors,
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onToggle
            )
        )
    }
}

@Composable
private fun JchuEnhancedSummary(
    expanded: Boolean,
    text: String,
    config: JchuExpandableDescriptionConfig,
    colors: JchuExpandableDescriptionColors,
    modifier: Modifier = Modifier
) {
    val animProgress by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        animationSpec = tween(config.animationDurationMs),
        label = "expand_animation"
    )
    Box(modifier) {
        SelectionContainer {
            Text(
                text = text,
                lineHeight = 20.sp,
                textAlign = config.textAlign,
                style = MaterialTheme.typography.bodySmall,
                maxLines = if (expanded) Int.MAX_VALUE else config.maxCollapsedLines,
                color = colors.contentColor.copy(alpha = 0.7f + (0.13f * animProgress)),
                overflow = if (expanded) TextOverflow.Visible else TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = if (expanded) 40.dp else 0.dp)
            )
        }
        if (text.length > config.minCharactersForExpansion) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .height(48.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, colors.gradientColor.copy(alpha = .9f))
                        )
                    ),
                contentAlignment = Alignment.BottomCenter
            ) {
                Icon(
                    painter = rememberAnimatedVectorPainter(
                        animatedImageVector = AnimatedImageVector.animatedVectorResource(
                            R.drawable.jchu_ic_deco_anim_arrow_down
                        ),
                        atEnd = !expanded
                    ),
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = colors.iconColor,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@Composable
private fun JchuExpandableImageGallery(
    images: List<String>,
    onImageClick: ((String, Int) -> Unit)?
) {
    LazyRow(
        contentPadding = PaddingValues(start = 15.dp, end = 15.dp, bottom = 15.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        state = rememberLazyListState()
    ) {
        itemsIndexed(images) { index, image ->
            NetworkImage(
                url = image,
                contentDescription = "Gallery image ${index + 1}",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onImageClick?.invoke(image, index) }
            )
        }
    }
}
