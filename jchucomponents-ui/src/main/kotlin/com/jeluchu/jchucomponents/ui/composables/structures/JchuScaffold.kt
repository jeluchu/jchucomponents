package com.jeluchu.jchucomponents.ui.composables.structures

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ui.R
import com.jeluchu.jchucomponents.ui.composables.button.FloatingButtonSettings
import com.jeluchu.jchucomponents.ui.composables.button.JchuFloatingButton
import com.jeluchu.jchucomponents.ui.composables.toolbars.CenterToolbarColors
import com.jeluchu.jchucomponents.ui.composables.toolbars.Toolbar
import com.jeluchu.jchucomponents.ui.composables.toolbars.TopBarSettings

@Composable
fun JchuScaffold(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentHorizontalPadding: Dp = 0.dp,
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    colors: JchuScaffoldColors = JchuScaffoldColors.materialDefaults(),
    content: @Composable (PaddingValues) -> Unit = {}
) {
    JchuScaffold(
        modifier = modifier,
        topBar = {
            Toolbar(
                title = title,
                topBarSettings =
                    TopBarSettings(
                        navIcon = colors.navigationIcon,
                        backgroundColor = colors.containerColor,
                        contentColor = colors.contentColor
                    ),
                navigateToBackScreen = onBackClick
            )
        },
        contentHorizontalPadding = contentHorizontalPadding,
        bottomBar = bottomBar,
        floatingActionButton = floatingActionButton,
        colors = colors,
        content = content
    )
}

@Composable
fun JchuScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    contentHorizontalPadding: Dp = 0.dp,
    colors: JchuScaffoldColors = JchuScaffoldColors.materialDefaults(),
    content: @Composable (PaddingValues) -> Unit = {}
) {
    val layoutDirection = LocalLayoutDirection.current

    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        floatingActionButton = floatingActionButton,
        containerColor = colors.containerColor,
        contentColor = colors.contentColor
    ) { paddingValues ->
        content(
            paddingValues.withHorizontalPadding(
                horizontalPadding = contentHorizontalPadding,
                layoutDirection = layoutDirection
            )
        )
    }
}

@Composable
fun JchuScrollableScaffold(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    contentHorizontalPadding: Dp = 0.dp,
    floatingButton: JchuFloatingButtonConfig = JchuFloatingButtonConfig(),
    colors: JchuScaffoldColors = JchuScaffoldColors.materialDefaults(),
    content: @Composable (PaddingValues, ScrollState) -> Unit = { _, _ -> }
) {
    JchuScaffold(
        title = title,
        onBackClick = onBackClick,
        modifier = modifier,
        contentHorizontalPadding = contentHorizontalPadding,
        colors = colors,
        floatingActionButton = {
            JchuScaffoldFloatingButton(
                config = floatingButton,
                isScrolling = scrollState.isScrollInProgress
            )
        }
    ) { paddingValues ->
        content(paddingValues, scrollState)
    }
}

@Composable
fun <T> JchuListScaffold(
    title: String,
    items: List<T>,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    contentHorizontalPadding: Dp = 0.dp,
    floatingButton: JchuFloatingButtonConfig = JchuFloatingButtonConfig(),
    colors: JchuScaffoldColors = JchuScaffoldColors.materialDefaults(),
    content: @Composable (PaddingValues, LazyListState, List<T>) -> Unit = { _, _, _ -> }
) {
    JchuScaffold(
        title = title,
        onBackClick = onBackClick,
        modifier = modifier,
        contentHorizontalPadding = contentHorizontalPadding,
        colors = colors,
        floatingActionButton = {
            JchuScaffoldFloatingButton(
                config = floatingButton,
                isScrolling = listState.isScrollInProgress
            )
        }
    ) { paddingValues ->
        content(paddingValues, listState, items)
    }
}

@Composable
fun <T> JchuGridScaffold(
    title: String,
    items: List<T>,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    gridState: LazyGridState = rememberLazyGridState(),
    contentHorizontalPadding: Dp = 0.dp,
    floatingButton: JchuFloatingButtonConfig = JchuFloatingButtonConfig(),
    colors: JchuScaffoldColors = JchuScaffoldColors.materialDefaults(),
    content: @Composable (PaddingValues, LazyGridState, List<T>) -> Unit = { _, _, _ -> }
) {
    JchuScaffold(
        title = title,
        onBackClick = onBackClick,
        modifier = modifier,
        contentHorizontalPadding = contentHorizontalPadding,
        colors = colors,
        floatingActionButton = {
            JchuScaffoldFloatingButton(
                config = floatingButton,
                isScrolling = gridState.isScrollInProgress
            )
        }
    ) { paddingValues ->
        content(paddingValues, gridState, items)
    }
}

@Composable
fun <T> JchuStateScaffold(
    title: String,
    items: List<T>,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentHorizontalPadding: Dp = 0.dp,
    colors: JchuScaffoldColors = JchuScaffoldColors.materialDefaults(),
    onSuccess: @Composable (PaddingValues, List<T>) -> Unit = { _, _ -> },
    onEmpty: @Composable (PaddingValues) -> Unit = {},
    onLoading: @Composable (PaddingValues) -> Unit = {}
) {
    JchuScaffold(
        title = title,
        onBackClick = onBackClick,
        modifier = modifier,
        contentHorizontalPadding = contentHorizontalPadding,
        colors = colors
    ) { paddingValues ->
        when {
            isLoading -> onLoading(paddingValues)
            items.isNotEmpty() -> onSuccess(paddingValues, items)
            else -> onEmpty(paddingValues)
        }
    }
}

@Composable
fun <T> JchuStateScaffold(
    title: String,
    item: T?,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    contentHorizontalPadding: Dp = 0.dp,
    colors: JchuScaffoldColors = JchuScaffoldColors.materialDefaults(),
    onSuccess: @Composable (PaddingValues, ScrollState, T) -> Unit = { _, _, _ -> },
    onEmpty: @Composable (PaddingValues) -> Unit = {},
    onLoading: @Composable (PaddingValues) -> Unit = {}
) {
    JchuScaffold(
        title = title,
        onBackClick = onBackClick,
        modifier = modifier,
        contentHorizontalPadding = contentHorizontalPadding,
        colors = colors
    ) { paddingValues ->
        when {
            isLoading -> onLoading(paddingValues)
            item != null -> onSuccess(paddingValues, scrollState, item)
            else -> onEmpty(paddingValues)
        }
    }
}

@Composable
private fun JchuScaffoldFloatingButton(
    config: JchuFloatingButtonConfig,
    isScrolling: Boolean
) {
    if (config.isActive) {
        JchuFloatingButton(
            isVisible = !isScrolling,
            floatButton = config.settings,
            contentDescription = config.contentDescription,
            onClick = config.onClick
        )
    }
}

@Immutable
class JchuScaffoldColors(
    val contentColor: Color,
    val containerColor: Color,
    @DrawableRes val navigationIcon: Int = R.drawable.ic_arrow_left
) {
    companion object {
        @Composable
        fun materialDefaults(
            contentColor: Color = MaterialTheme.colorScheme.onBackground,
            containerColor: Color = MaterialTheme.colorScheme.background,
            @DrawableRes navigationIcon: Int = R.drawable.ic_arrow_left
        ) = JchuScaffoldColors(
            contentColor = contentColor,
            containerColor = containerColor,
            navigationIcon = navigationIcon
        )
    }
}

@Immutable
class JchuFloatingButtonConfig(
    val isActive: Boolean = false,
    val settings: FloatingButtonSettings = FloatingButtonSettings(),
    val contentDescription: String? = null,
    val onClick: () -> Unit = {}
)

fun JchuScaffoldColors.toCenterToolbarColors() =
    CenterToolbarColors(
        contentColor = contentColor,
        containerColor = containerColor
    )

private fun PaddingValues.withHorizontalPadding(
    horizontalPadding: Dp,
    layoutDirection: LayoutDirection
): PaddingValues =
    PaddingValues(
        start = calculateStartPadding(layoutDirection) + horizontalPadding,
        top = calculateTopPadding(),
        end = calculateEndPadding(layoutDirection) + horizontalPadding,
        bottom = calculateBottomPadding()
    )
