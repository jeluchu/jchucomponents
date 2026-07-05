package com.jeluchu.jchucomponents.ui.composables.structures

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private val previewItems = listOf("Villagers", "Furniture", "Fossils", "Music")

@Preview(showBackground = true)
@Composable
private fun JchuScaffoldPreview() {
    JchuPreviewSurface {
        JchuScaffold(
            title = "Scaffold",
            onBackClick = {},
            contentHorizontalPadding = 16.dp
        ) { paddingValues ->
            JchuPreviewContent(
                modifier = Modifier.padding(paddingValues),
                title = "Base scaffold"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun JchuScrollableScaffoldPreview() {
    JchuPreviewSurface {
        JchuScrollableScaffold(
            title = "Scrollable",
            onBackClick = {},
            contentHorizontalPadding = 16.dp
        ) { paddingValues, _ ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(previewItems) { item ->
                    JchuPreviewCard(item)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun JchuListScaffoldPreview() {
    JchuPreviewSurface {
        JchuListScaffold(
            title = "List",
            items = previewItems,
            onBackClick = {},
            contentHorizontalPadding = 16.dp
        ) { paddingValues, _, items ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { item ->
                    JchuPreviewCard(item)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun JchuGridScaffoldPreview() {
    JchuPreviewSurface {
        JchuGridScaffold(
            title = "Grid",
            items = previewItems,
            onBackClick = {},
            contentHorizontalPadding = 16.dp
        ) { paddingValues, _, items ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { item ->
                    JchuPreviewCard(item)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun JchuStateScaffoldPreview() {
    JchuPreviewSurface {
        JchuStateScaffold(
            title = "State",
            items = previewItems,
            isLoading = false,
            onBackClick = {},
            contentHorizontalPadding = 16.dp,
            onSuccess = { paddingValues, items ->
                Column(
                    modifier = Modifier.padding(paddingValues),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items.forEach { item -> JchuPreviewCard(item) }
                }
            },
            onEmpty = { JchuDefaultEmptyContent() },
            onLoading = { JchuDefaultLoadingContent() }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun JchuDetailsScaffoldPreview() {
    JchuPreviewSurface {
        JchuDetailsScaffold(
            title = "Details",
            details = "A reusable detail scaffold",
            isLoading = false,
            onBackClick = {}
        ) { value ->
            item { JchuPreviewCard(value) }
            item { JchuPreviewCard("Extra detail") }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun JchuPurchaseElementsScaffoldPreview() {
    val query = remember { mutableStateOf("") }
    val theme = JchuAppColorThemes.nooksCranny

    JchuPreviewSurface {
        JchuPurchaseElementsScaffold(
            title = "Purchase Items",
            items = previewItems,
            isLoading = false,
            onBackClick = {},
            key = { it },
            config = theme.toPurchaseScaffoldConfig(query = query)
        ) { type, item ->
            JchuPreviewCard("$item ${type.name}")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun JchuPurchaseTabItemsScaffoldPreview() {
    val query = remember { mutableStateOf("") }
    val theme = JchuAppColorThemes.fossils

    JchuPreviewSurface {
        JchuPurchaseTabItemsScaffold(
            title = "Purchase Tabs",
            tabs = previewTabs(),
            isLoading = false,
            error = null,
            onBackClick = {},
            config = theme.toPurchaseTabScaffoldConfig(query = query)
        ) { paddingValues, selectedIndex ->
            JchuPreviewContent(
                modifier = Modifier.padding(paddingValues),
                title = "Purchase tab $selectedIndex"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun JchuShareScaffoldPreview() {
    JchuPreviewSurface {
        JchuShareScaffold(
            title = "Share",
            isLoading = false,
            onBackClick = {},
            onShareClick = {},
            onDownloadClick = {},
            config = JchuAppColorThemes.avatarCreator.toShareScaffoldConfig()
        ) { paddingValues ->
            JchuPreviewContent(
                modifier = Modifier.padding(paddingValues),
                title = "Shareable content"
            )
        }
    }
}

@Composable
private fun JchuPreviewSurface(content: @Composable () -> Unit) {
    MaterialTheme {
        Surface(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
            content = content
        )
    }
}

@Composable
private fun JchuPreviewContent(
    title: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        JchuPreviewCard("Preview row")
    }
}

@Composable
private fun JchuPreviewCard(text: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp)
        )
    }
}

private fun previewTabs() =
    listOf(
        JchuScaffoldTabItem(label = "One"),
        JchuScaffoldTabItem(label = "Two"),
        JchuScaffoldTabItem(label = "Three")
    )
