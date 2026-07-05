package com.jeluchu.jchucomponents.ui.composables.structures

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ui.composables.textfields.JchuExpandableSearch
import com.jeluchu.jchucomponents.ui.composables.toolbars.Toolbar
import com.jeluchu.jchucomponents.ui.composables.toolbars.TopBarSettings

enum class JchuPurchaseItemType {
    Horizontal,
    Vertical
}

@Composable
fun <T> JchuPurchaseElementsScaffold(
    title: String,
    items: List<T>?,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    cells: Int = 2,
    key: ((item: T) -> Any)? = null,
    error: String? = null,
    filtered: (List<T>) -> List<T> = { it },
    listState: LazyListState = rememberLazyListState(),
    gridState: LazyGridState = rememberLazyGridState(),
    isColumnList: Boolean = false,
    config: JchuPurchaseScaffoldConfig = JchuPurchaseScaffoldConfig(),
    topContent: (@Composable () -> Unit)? = null,
    headerContent: (@Composable () -> Unit)? = null,
    content: @Composable (JchuPurchaseItemType, T) -> Unit = { _, _ -> }
) {
    Scaffold(
        modifier = modifier,
        containerColor = config.scaffoldColors.containerColor,
        contentColor = config.scaffoldColors.contentColor,
        topBar = {
            Column {
                Toolbar(
                    title = title,
                    topBarSettings =
                        TopBarSettings(
                            contentColor = config.scaffoldColors.contentColor,
                            backgroundColor = config.scaffoldColors.containerColor
                        ),
                    navigateToBackScreen = onBackClick
                )
                topContent?.invoke()
            }
        },
        bottomBar = {
            JchuPurchaseSearchBottomBar(
                isVisible =
                    !isLoading &&
                        error.isNullOrEmpty() &&
                        items?.isNotEmpty() == true &&
                        config.searchConfig.isActive,
                config = config
            )
        }
    ) { paddingValues ->
        JchuPurchaseStates(
            cells = cells,
            items = items,
            isLoading = isLoading,
            error = error,
            key = key,
            paddingValues = paddingValues,
            modifier = modifier,
            filtered = filtered,
            headerContent = headerContent,
            listState = listState,
            gridState = gridState,
            isColumnList = isColumnList,
            config = config,
            content = content
        )
    }
}

@Composable
fun <T> JchuPurchaseStates(
    cells: Int = 2,
    items: List<T>?,
    isLoading: Boolean,
    error: String?,
    key: ((item: T) -> Any)? = null,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    filtered: (List<T>) -> List<T> = { it },
    headerContent: (@Composable () -> Unit)? = null,
    listState: LazyListState = rememberLazyListState(),
    gridState: LazyGridState = rememberLazyGridState(),
    isColumnList: Boolean = false,
    config: JchuPurchaseScaffoldConfig = JchuPurchaseScaffoldConfig(),
    content: @Composable (JchuPurchaseItemType, T) -> Unit = { _, _ -> }
) {
    JchuRemoteScreenContent(
        data = items,
        isLoading = isLoading,
        error = error,
        onLoading = { JchuDefaultLoadingContent() },
        onFailure = config.errorContent,
        onSuccess = { sourceItems ->
            val filteredItems = filtered(sourceItems)
            when {
                config.headerConfig.isCompletedByHiddenFavorites &&
                    config.searchConfig.query.value
                        .isEmpty() -> config.hiddenFavoritesContent()

                filteredItems.isEmpty() -> config.emptyContent()

                isColumnList ->
                    JchuPurchaseColumnContent(
                        key = key,
                        config = config,
                        modifier = modifier,
                        listState = listState,
                        items = filteredItems,
                        headerContent = headerContent,
                        paddingValues = paddingValues,
                        content = { content(JchuPurchaseItemType.Horizontal, it) }
                    )

                else ->
                    JchuPurchaseGridContent(
                        key = key,
                        cells = cells,
                        config = config,
                        modifier = modifier,
                        items = filteredItems,
                        gridState = gridState,
                        headerContent = headerContent,
                        paddingValues = paddingValues,
                        content = { content(JchuPurchaseItemType.Vertical, it) }
                    )
            }
        }
    )
}

@Composable
fun JchuPurchaseTabItemsScaffold(
    title: String,
    tabs: List<JchuScaffoldTabItem>,
    isLoading: Boolean,
    error: String?,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedTabIndex: Int = 0,
    isScroll: Boolean = false,
    config: JchuPurchaseTabScaffoldConfig = JchuPurchaseTabScaffoldConfig(),
    onTabSelected: (Int) -> Unit = {},
    tabBar: @Composable (Int, List<JchuScaffoldTabItem>, (Int) -> Unit) -> Unit = { index, items, onSelected ->
        JchuPurchaseTabBar(
            selectedIndex = index,
            tabs = items,
            onSelected = onSelected,
            colors = config.tabColors
        )
    },
    content: @Composable (PaddingValues, Int) -> Unit = { _, _ -> }
) {
    var internalSelectedIndex by rememberSaveable { mutableIntStateOf(selectedTabIndex) }
    val selectedIndex = internalSelectedIndex.coerceIn(0, tabs.lastIndex.coerceAtLeast(0))

    Scaffold(
        modifier = modifier,
        topBar = {
            Toolbar(
                title = title,
                topBarSettings =
                    TopBarSettings(
                        contentColor = config.scaffoldColors.contentColor,
                        backgroundColor = config.scaffoldColors.containerColor
                    ),
                navigateToBackScreen = onBackClick
            )
        },
        bottomBar = {
            Column(
                modifier =
                    Modifier
                        .animateContentSize()
                        .imePadding(),
                verticalArrangement = Arrangement.Bottom
            ) {
                JchuPurchaseTabSearchBar(
                    isVisible =
                        config.searchConfig.isActive &&
                            !isLoading &&
                            error.isNullOrEmpty() &&
                            tabs.isNotEmpty(),
                    config = config
                )

                if (tabs.isNotEmpty()) {
                    tabBar(selectedIndex, tabs) { index ->
                        internalSelectedIndex = index
                        onTabSelected(index)
                    }
                }
            }
        },
        containerColor = config.scaffoldColors.containerColor,
        contentColor = config.scaffoldColors.contentColor
    ) { paddingValues ->
        when {
            isLoading -> JchuDefaultLoadingContent()
            !error.isNullOrEmpty() || tabs.isEmpty() -> JchuDefaultEmptyContent()
            else -> content(paddingValues, selectedIndex)
        }
    }
}

@Composable
fun JchuPurchaseTabBar(
    selectedIndex: Int,
    tabs: List<JchuScaffoldTabItem>,
    onSelected: (Int) -> Unit,
    colors: JchuPurchaseTabColors = JchuPurchaseTabColors()
) {
    NavigationBar(
        containerColor = colors.containerColor,
        contentColor = colors.unselectedContentColor
    ) {
        tabs.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { onSelected(index) },
                icon = item.icon,
                label = { Text(item.label) },
                alwaysShowLabel = item.alwaysShowLabel,
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = colors.selectedContentColor,
                        selectedTextColor = colors.selectedContentColor,
                        indicatorColor = colors.selectedContainerColor,
                        unselectedIconColor = colors.unselectedContentColor,
                        unselectedTextColor = colors.unselectedContentColor
                    )
            )
        }
    }
}

@Composable
private fun <T> JchuPurchaseColumnContent(
    items: List<T>,
    modifier: Modifier,
    key: ((item: T) -> Any)?,
    listState: LazyListState,
    paddingValues: PaddingValues,
    config: JchuPurchaseScaffoldConfig,
    content: @Composable (T) -> Unit,
    headerContent: (@Composable () -> Unit)?
) {
    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = JchuPurchaseContentPadding(paddingValues)
    ) {
        item { JchuPurchaseHeader(config = config) }
        headerContent?.let { item { it() } }
        items(items, key = key) { item ->
            content(item)
        }
    }
}

@Composable
private fun <T> JchuPurchaseGridContent(
    cells: Int,
    items: List<T>,
    modifier: Modifier,
    key: ((item: T) -> Any)?,
    gridState: LazyGridState,
    paddingValues: PaddingValues,
    config: JchuPurchaseScaffoldConfig,
    content: @Composable (T) -> Unit,
    headerContent: (@Composable () -> Unit)?
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(cells),
        state = gridState,
        modifier = modifier.fillMaxSize(),
        contentPadding = JchuPurchaseContentPadding(paddingValues, includeBottom = false),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            JchuPurchaseHeader(config = config)
        }
        headerContent?.let {
            item(span = { GridItemSpan(maxLineSpan) }) { it() }
        }
        items(items, key = key) { item ->
            content(item)
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(
                modifier =
                    Modifier.height(
                        paddingValues.calculateBottomPadding() + 20.dp
                    )
            )
        }
    }
}

@Composable
private fun JchuPurchaseHeader(config: JchuPurchaseScaffoldConfig) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        config.headerConfig.progressContent?.invoke()
        config.headerConfig.markAllContent?.invoke()
    }
}

@Composable
private fun JchuPurchaseSearchBottomBar(
    isVisible: Boolean,
    config: JchuPurchaseScaffoldConfig
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(),
        exit = scaleOut(),
        modifier =
            Modifier.background(
                brush =
                    Brush.verticalGradient(
                        colors =
                            listOf(
                                Color.Transparent,
                                config.scaffoldColors.containerColor
                            ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
            )
    ) {
        JchuExpandableSearch(
            query = config.searchConfig.query.value,
            onQueryChange = { config.searchConfig.query.value = it },
            defaults = config.searchConfig.toSearchBarDefaults(),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .padding(bottom = 30.dp)
                    .navigationBarsPadding()
                    .imePadding()
        )
    }
}

@Composable
private fun JchuPurchaseTabSearchBar(
    isVisible: Boolean,
    config: JchuPurchaseTabScaffoldConfig
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(),
        exit = scaleOut()
    ) {
        JchuExpandableSearch(
            query = config.searchConfig.query.value,
            onQueryChange = { config.searchConfig.query.value = it },
            defaults = config.searchConfig.toSearchBarDefaults(),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
        )
    }
}

private fun JchuPurchaseContentPadding(
    paddingValues: PaddingValues,
    includeBottom: Boolean = true
): PaddingValues =
    PaddingValues(
        start = 15.dp,
        end = 15.dp,
        top = paddingValues.calculateTopPadding(),
        bottom = if (includeBottom) paddingValues.calculateBottomPadding() + 20.dp else 15.dp
    )
