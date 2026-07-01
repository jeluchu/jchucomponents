package com.jeluchu.composer.features.loaders.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jeluchu.composer.core.ui.composables.ScaffoldStructure
import com.jeluchu.jchucomponents.ui.composables.loaders.CircularLoading
import com.jeluchu.jchucomponents.ui.composables.loaders.ProgressIndicator
import com.jeluchu.jchucomponents.ui.composables.loaders.PulseLoading

@Composable
fun LoadersView(onBack: () -> Unit) {
    ScaffoldStructure(
        title = "Loaders",
        onNavIconClick = onBack
    ) {
        Text("Circular", style = MaterialTheme.typography.titleMedium)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
        ) {
            CircularLoading(
                isShow = true,
                colorLoading = MaterialTheme.colorScheme.primary
            )
        }

        Text("Dots", style = MaterialTheme.typography.titleMedium)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp),
            contentAlignment = Alignment.Center
        ) {
            ProgressIndicator(
                modifier = Modifier.height(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }

        Text("Pulse", style = MaterialTheme.typography.titleMedium)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            PulseLoading(
                maxPulseSize = 140f,
                minPulseSize = 40f
            )
        }
    }
}
