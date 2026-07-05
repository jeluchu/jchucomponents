package com.jeluchu.jchucomponents.ui.foundation.lists

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private val previewItems = arrayOf("One", "Two", "Three", "Four", "Five", "Six")

@Preview(showBackground = true, heightDp = 240)
@Composable
private fun LazyColumnForPreview() {
    LazyColumnFor(items = previewItems) { item ->
        PreviewListItem(item)
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
private fun LazyRowForPreview() {
    LazyRowFor(items = previewItems) { item ->
        PreviewListItem(item)
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
private fun LazyGridPreview() {
    LazyGrid(items = previewItems.toList()) { item, _ ->
        PreviewListItem(item)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true, widthDp = 320, heightDp = 120)
@Composable
private fun LazyHorizontalGridPreview() {
    LazyHorizontalGrid(
        cells = GridCells.Fixed(2),
        modifier = Modifier.size(width = 320.dp, height = 120.dp)
    ) {
        items(previewItems) { item ->
            PreviewListItem(item)
        }
    }
}

@Composable
private fun PreviewListItem(item: String) {
    Text(
        text = item,
        modifier =
            Modifier
                .padding(4.dp)
                .background(Color.LightGray, MaterialTheme.shapes.small)
                .padding(12.dp)
    )
}
