package com.jeluchu.jchucomponents.ui.composables.progress

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
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

@Preview(showBackground = true)
@Composable
private fun JchuProgressPreview() {
    val state = JchuProgressState(
        title = "Upload",
        value = 72.0,
        maxValue = 100.0,
        isEnabled = true,
        isIndeterminate = false
    )

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        JchuLinearProgress(
            state = state,
            icon = Icons.Default.CloudUpload
        )
        JchuCircularProgress(
            state = state,
            icon = Icons.Default.CloudUpload
        )
        JchuIconProgress(
            state = state,
            icon = Icons.Default.CloudUpload
        )
    }
}
