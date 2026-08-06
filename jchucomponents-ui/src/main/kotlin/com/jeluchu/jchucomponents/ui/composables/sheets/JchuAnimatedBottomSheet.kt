package com.jeluchu.jchucomponents.ui.composables.sheets

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.node.Ref
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Displays a modal bottom sheet while [value] is non-null.
 *
 * The last non-null value is retained while the sheet animates out, preserving the behavior of
 * the component extracted from iNook.
 *
 * @param value state that controls visibility and is provided to [content].
 * @param onDismissRequest called only for user-driven dismissal requests.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> JchuAnimatedBottomSheet(
    value: T?,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    sheetState: SheetState = rememberModalBottomSheetState(),
    sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = 0.dp,
    sheetGesturesEnabled: Boolean = true,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    contentWindowInsets: @Composable () -> WindowInsets = { BottomSheetDefaults.windowInsets },
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties,
    content: @Composable ColumnScope.(T & Any) -> Unit
) {
    LaunchedEffect(value != null) {
        if (value != null) {
            sheetState.show()
        } else if (sheetState.isVisible) {
            sheetState.hide()
        }
    }

    if (!sheetState.isVisible && value == null) return

    ModalBottomSheet(
        shape = shape,
        modifier = modifier,
        properties = properties,
        scrimColor = scrimColor,
        sheetState = sheetState,
        sheetMaxWidth = sheetMaxWidth,
        dragHandle = dragHandle,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        onDismissRequest = onDismissRequest,
        contentWindowInsets = contentWindowInsets,
        sheetGesturesEnabled = sheetGesturesEnabled
    ) {
        val notNullValue = lastNotNullValueOrNull(value) ?: return@ModalBottomSheet
        content(notNullValue)
    }
}

@Composable
private fun <T> lastNotNullValueOrNull(value: T?): T? {
    val lastNotNullValueOrNullRef = remember { Ref<T>() }
    return value?.also {
        lastNotNullValueOrNullRef.value = it
    } ?: lastNotNullValueOrNullRef.value
}
