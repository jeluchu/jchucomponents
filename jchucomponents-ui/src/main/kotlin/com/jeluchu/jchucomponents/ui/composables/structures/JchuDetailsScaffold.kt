package com.jeluchu.jchucomponents.ui.composables.structures

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.ui.composables.toolbars.CenterToolbar

@Composable
fun <T> JchuDetailsScaffold(
    title: String,
    details: T?,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
    bottomBar: @Composable () -> Unit = {},
    listState: LazyListState = rememberLazyListState(),
    config: JchuDetailsScaffoldConfig = JchuDetailsScaffoldConfig(),
    onEmpty: @Composable (PaddingValues) -> Unit = { JchuDefaultEmptyContent() },
    onLoading: @Composable (PaddingValues) -> Unit = { JchuDefaultLoadingContent() },
    content: LazyListScope.(T) -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        bottomBar = bottomBar,
        containerColor = config.colors.containerColor,
        contentColor = config.colors.contentColor,
        topBar = {
            CenterToolbar(
                title = { Text(text = title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = ImageVector.vectorResource(config.colors.navigationIcon),
                            contentDescription = null
                        )
                    }
                },
                colors = config.colors.toCenterToolbarColors()
            )
        }
    ) { paddingValues ->
        when {
            isLoading -> onLoading(paddingValues)
            !error.isNullOrEmpty() || details == null -> onEmpty(paddingValues)
            else ->
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(config.itemSpacing),
                    contentPadding =
                        PaddingValues(
                            start = config.contentHorizontalPadding,
                            top = paddingValues.calculateTopPadding(),
                            end = config.contentHorizontalPadding,
                            bottom = paddingValues.calculateBottomPadding() + config.contentBottomPadding
                        ),
                    content = { content(details) }
                )
        }
    }
}

@Immutable
class JchuDetailsScaffoldConfig(
    val colors: JchuScaffoldColors =
        JchuScaffoldColors(
            contentColor = androidx.compose.ui.graphics.Color.Black,
            containerColor = androidx.compose.ui.graphics.Color.White
        ),
    val itemSpacing: Dp = 10.dp,
    val contentHorizontalPadding: Dp = 15.dp,
    val contentBottomPadding: Dp = 20.dp
)
