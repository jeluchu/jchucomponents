package com.jeluchu.jchucomponents.ui.composables.textfields

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Expandable search field with internally managed expansion state.
 */
@Composable
fun JchuExpandableSearch(
    query: String,
    onQueryChange: (String) -> Unit,
    defaults: SearchBarDefaults,
    modifier: Modifier = Modifier,
    onExpandedChange: (Boolean) -> Unit = {},
    onSearch: (String) -> Unit = {}
) {
    var expanded by rememberSaveable {
        mutableStateOf(defaults.initiallyExpanded)
    }

    JchuExpandableSearch(
        query = query,
        onQueryChange = onQueryChange,
        expanded = expanded,
        onExpandedChange = {
            expanded = it
            onExpandedChange(it)
        },
        defaults = defaults,
        modifier = modifier,
        onSearch = onSearch
    )
}

/**
 * Expandable search field whose expansion state is owned by the caller.
 */
@Composable
fun JchuExpandableSearch(
    query: String,
    onQueryChange: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    defaults: SearchBarDefaults,
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val containerColor =
        defaults.containerColor.takeOrElse {
            MaterialTheme.colorScheme.surfaceContainer
        }
    val contentColor =
        defaults.contentColor.takeOrElse {
            MaterialTheme.colorScheme.onSurface
        }
    val cursorColor =
        defaults.cursorColor.takeOrElse {
            MaterialTheme.colorScheme.primary
        }

    fun close() {
        onExpandedChange(false)
        focusManager.clearFocus()
        keyboardController?.hide()
    }

    LaunchedEffect(expanded) {
        if (expanded) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier =
                Modifier
                    .then(if (expanded) Modifier.fillMaxWidth() else Modifier.wrapContentWidth())
                    .animateContentSize(
                        animationSpec =
                            spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                    ),
            color = containerColor,
            contentColor = contentColor,
            shape = defaults.shape
        ) {
            AnimatedContent(
                targetState = expanded,
                transitionSpec = {
                    (
                        fadeIn(animationSpec = tween(180)) +
                            scaleIn(
                                initialScale = 0.96f,
                                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                            )
                    ) togetherWith (
                        fadeOut(animationSpec = tween(120)) +
                            scaleOut(targetScale = 0.98f, animationSpec = tween(120))
                    ) using
                        SizeTransform(
                            clip = false,
                            sizeAnimationSpec = { _, _ ->
                                spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            }
                        )
                },
                label = "JchuExpandableSearch"
            ) { isExpanded ->
                if (isExpanded) {
                    TextField(
                        value = query,
                        onValueChange = onQueryChange,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                        textStyle = defaults.textStyle,
                        leadingIcon = {
                            Icon(
                                imageVector = defaults.searchIcon,
                                contentDescription = defaults.searchContentDescription
                            )
                        },
                        trailingIcon = {
                            Row(
                                modifier =
                                    Modifier
                                        .background(
                                            color = contentColor.copy(alpha = 0.08f),
                                            shape = RoundedCornerShape(12.dp)
                                        ).animateContentSize()
                                        .padding(horizontal = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                AnimatedVisibility(
                                    visible = query.isNotEmpty(),
                                    enter = fadeIn() + scaleIn(initialScale = 0.7f),
                                    exit = fadeOut() + scaleOut(targetScale = 0.7f)
                                ) {
                                    IconButton(onClick = { onQueryChange("") }) {
                                        Icon(
                                            imageVector = defaults.clearIcon,
                                            contentDescription = defaults.clearContentDescription
                                        )
                                    }
                                }

                                IconButton(onClick = ::close) {
                                    Icon(
                                        imageVector = defaults.closeIcon,
                                        contentDescription = defaults.closeContentDescription
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        keyboardActions =
                            KeyboardActions(
                                onSearch = {
                                    onSearch(query)
                                    close()
                                }
                            ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        colors =
                            TextFieldDefaults.colors(
                                focusedTextColor = contentColor,
                                unfocusedTextColor = contentColor,
                                focusedContainerColor = containerColor,
                                unfocusedContainerColor = containerColor,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = cursorColor
                            )
                    )
                } else {
                    Row(
                        modifier =
                            Modifier
                                .clickable { onExpandedChange(true) }
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = defaults.searchIcon,
                            contentDescription = defaults.searchContentDescription,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = defaults.label,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

/**
 * Visual and behavioural defaults shared by controlled and uncontrolled search bars.
 *
 * Unspecified colors inherit the active Material theme.
 */
@Immutable
data class SearchBarDefaults(
    val label: String,
    val initiallyExpanded: Boolean = false,
    val containerColor: Color = Color.Unspecified,
    val contentColor: Color = Color.Unspecified,
    val cursorColor: Color = Color.Unspecified,
    val shape: Shape = RoundedCornerShape(18.dp),
    val textStyle: TextStyle = TextStyle.Default,
    val searchIcon: ImageVector = Icons.Default.Search,
    val clearIcon: ImageVector = Icons.Default.Clear,
    val closeIcon: ImageVector = Icons.Default.Close,
    val searchContentDescription: String = label,
    val clearContentDescription: String = "Clear search",
    val closeContentDescription: String = "Close search"
)

@Preview(showBackground = true)
@Composable
private fun JchuExpandableSearchPreview() {
    var query by remember { mutableStateOf("Compose") }

    JchuExpandableSearch(
        query = query,
        onQueryChange = { query = it },
        defaults =
            SearchBarDefaults(
                label = "Search",
                initiallyExpanded = true
            ),
        modifier = Modifier.padding(16.dp)
    )
}
