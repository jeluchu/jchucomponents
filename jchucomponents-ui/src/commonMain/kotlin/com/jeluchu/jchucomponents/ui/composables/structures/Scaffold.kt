package com.jeluchu.jchucomponents.ui.composables.structures

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 *
 * Author: @Jeluchu
 *
 * This component is useful to check the status of a response to a request to the service,
 * and depending on the result it can display information, an error or the loading progress
 *
 * @param modifier optional Modifier for the root of the [Scaffold]
 * @param snackbarHostState state used to show snackbars through [snackbarHost]
 * @param topBar top app bar of the screen. Consider using [TopAppBar].
 * @param bottomBar bottom bar of the screen. Consider using [BottomAppBar].
 * @param snackbarHost component to host [Snackbar]s that are pushed to be shown via
 * [SnackbarHostState.showSnackbar]. Receives [snackbarHostState]; usually it's a [SnackbarHost]
 * @param floatingActionButton Main action button of your screen. Consider using
 * [FloatingActionButton] for this slot.
 * @param floatingActionButtonPosition position of the FAB on the screen. See [FabPosition] for
 * possible options available.
 * @param containerColor background of the scaffold body
 * @param contentColor color of the content in scaffold body. Defaults to either the matching
 * content color for [containerColor], or, if it is not a color from the theme, this will keep
 * the same value set above this Surface.
 * @param isLoading controls the status of whether the request to the service is loading
 * or has already finished.
 * @param items the list of items to be displayed within the LazyColumn or LazyRow
 * @param onSuccess content when service response is success of your screen. The lambda receives an
 * [PaddingValues] that should be applied to the content root via Modifier.padding to
 * properly offset top and bottom bars. If you're using VerticalScroller, apply this
 * modifier to the child of the scroller, and not on the scroller itself.
 * @param onFailed content when service response is failed of your screen. The lambda receives an
 * [PaddingValues] that should be applied to the content root via Modifier.padding to
 * properly offset top and bottom bars. If you're using VerticalScroller, apply this
 * modifier to the child of the scroller, and not on the scroller itself.
 * @param onLoading content when service response is loading of your screen. The lambda receives an
 * [PaddingValues] that should be applied to the content root via Modifier.padding to
 * properly offset top and bottom bars. If you're using VerticalScroller, apply this
 * modifier to the child of the scroller, and not on the scroller itself.
 *
 */
@Composable
fun <T> ScaffoldStates(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    isLoading: Boolean = false,
    items: List<T> = emptyList(),
    onSuccess: @Composable (PaddingValues, List<T>) -> Unit,
    onFailed: @Composable (PaddingValues) -> Unit = {},
    onLoading: @Composable (PaddingValues) -> Unit = {}
) = Scaffold(
    modifier = modifier,
    topBar = topBar,
    bottomBar = bottomBar,
    snackbarHost = { snackbarHost(snackbarHostState) },
    floatingActionButton = floatingActionButton,
    floatingActionButtonPosition = floatingActionButtonPosition,
    containerColor = containerColor,
    contentColor = contentColor
) { paddingValues ->
    if (isLoading) onLoading(paddingValues)
    if (items.isNotEmpty()) onSuccess(paddingValues, items)
    if (!isLoading && items.isEmpty()) onFailed(paddingValues)
}
