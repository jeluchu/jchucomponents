package com.jeluchu.jchucomponents.ui.composables.progress

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.foundation.components.JchuProgressState

@Composable
fun JchuLinearProgress(
    state: JchuProgressState,
    icon: ImageVector,
) {
    if (state.isIndeterminate) {
        LinearProgressIndicator()
    } else {
        LinearProgressbar(
            icon = icon,
            enabled = state.isEnabled,
            number = state.value.toFloat(),
            maxNumber = state.maxValue.toFloat(),
        )
    }
}

@Composable
fun JchuCircularProgress(
    state: JchuProgressState,
    icon: ImageVector,
) {
    if (state.isIndeterminate) {
        CircularProgressIndicator(modifier = Modifier.size(45.dp))
    } else {
        CircularProgressbar(
            icon = icon,
            enabled = state.isEnabled,
            number = state.value.toFloat(),
            maxNumber = state.maxValue.toFloat(),
        )
    }
}

@Composable
fun JchuIconProgress(
    state: JchuProgressState,
    icon: ImageVector,
) {
    if (state.isIndeterminate) {
        CircularProgressIndicator()
    } else {
        IconProgress(
            icon = icon,
            enabled = state.isEnabled,
            number = state.value.toFloat(),
            maxNumber = state.maxValue.toFloat(),
        )
    }
}
