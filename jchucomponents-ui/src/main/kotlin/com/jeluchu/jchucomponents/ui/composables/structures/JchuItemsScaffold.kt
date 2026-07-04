package com.jeluchu.jchucomponents.ui.composables.structures

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding

@Composable
fun JchuDefaultLoadingContent(
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.CircularProgressIndicator(
        modifier = modifier.padding(24.dp)
    )
}

@Composable
fun JchuDefaultEmptyContent(
    text: String = "No items",
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier.padding(24.dp),
        style = MaterialTheme.typography.bodyMedium
    )
}

@Immutable
class JchuScaffoldTabItem(
    val label: String,
    val icon: @Composable () -> Unit = {},
    val alwaysShowLabel: Boolean = true
)

@Preview(showBackground = true)
@Composable
private fun JchuDefaultLoadingContentPreview() {
    JchuDefaultLoadingContent()
}

@Preview(showBackground = true)
@Composable
private fun JchuDefaultEmptyContentPreview() {
    JchuDefaultEmptyContent()
}
