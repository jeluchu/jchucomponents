package com.jeluchu.composer.features.navigationcodegen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jeluchu.composer.core.catalog.JchuCatalogTheme
import com.jeluchu.composer.core.ui.composables.ScaffoldStructure
import com.jeluchu.composer.features.navigationcodegen.domain.Anime
import com.jeluchu.composer.features.navigationcodegen.navigation.NavigationCodegenRoutes
import com.jeluchu.composer.features.navigationcodegen.viewmodel.AnimeDetailsViewModel
import com.jeluchu.composer.features.navigationcodegen.viewmodel.AnimeSearchUiState
import com.jeluchu.composer.features.navigationcodegen.viewmodel.AnimeSearchViewModel
import com.jeluchu.jchucomponents.navigation3.BackStackBack
import com.jeluchu.jchucomponents.navigation3.NavigateTo
import com.jeluchu.jchucomponents.navigation3.Screen
import com.jeluchu.jchucomponents.navigation3.ScreenKoinViewModel
import com.jeluchu.jchucomponents.ui.composables.toolbars.CenterToolbarColors

@Composable
@Screen(graph = "NavigationCodegen")
internal fun AnimeSearchRoute(
    @BackStackBack onBack: () -> Unit,
    @NavigateTo(route = NavigationCodegenRoutes.AnimeDetailsRoute::class)
    onOpenDetails: (animeId: Int, title: String) -> Unit,
    @ScreenKoinViewModel
    viewModel: AnimeSearchViewModel
) {
    val state by viewModel.state.collectAsState()
    val colors = JchuCatalogTheme.colors

    ScaffoldStructure(
        title = "Anime · Tenrai",
        onNavIconClick = onBack,
        colors = CenterToolbarColors(
            containerColor = colors.background,
            contentColor = colors.content
        )
    ) {
        Text("Búsqueda y detalle con MVVM", color = colors.accent, fontWeight = FontWeight.Bold)
        Text("Los datos llegan desde la API pública de Tenrai.", color = colors.content)
        OutlinedTextField(
            value = state.query,
            onValueChange = viewModel::onQueryChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Título de anime") },
            singleLine = true
        )
        Button(
            onClick = viewModel::search,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Buscar")
        }
        AnimeSearchContent(
            state = state,
            onAnimeClick = { anime -> onOpenDetails(anime.id, anime.title) }
        )
    }
}

@Screen(graph = "NavigationCodegen")
@Composable
internal fun AnimeDetailsRoute(
    animeId: Int,
    title: String,
    @BackStackBack onBack: () -> Unit,
    @ScreenKoinViewModel
    viewModel: AnimeDetailsViewModel
) {
    val state by viewModel.state.collectAsState()
    val colors = JchuCatalogTheme.colors
    val anime = state.anime

    ScaffoldStructure(
        title = "Detalle de anime",
        onNavIconClick = onBack,
        colors = CenterToolbarColors(
            containerColor = colors.background,
            contentColor = colors.content
        )
    ) {
        Text(anime.title, color = colors.content, fontWeight = FontWeight.ExtraBold)
        if (anime.imageUrl != null) {
            AsyncImage(
                model = anime.imageUrl,
                contentDescription = anime.title,
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Text("ID de MyAnimeList: ${anime.id}", color = colors.content)
        Text(
            text = listOfNotNull(
                anime.type,
                anime.episodes?.let { "$it episodios" },
                anime.year?.toString()
            ).joinToString(" · ").ifBlank { "Datos de emisión no disponibles" },
            color = colors.accent
        )
        anime.score?.let { Text("Puntuación media: $it", color = colors.content) }
        if (state.isLoading) CircularProgressIndicator()
        state.errorMessage?.let { Text(it, color = colors.error) }
        if (anime.genres.isNotEmpty()) {
            Text("Géneros: ${anime.genres.joinToString()}", color = colors.content)
        }
        Text(anime.synopsis ?: "Sinopsis no disponible.", color = colors.content)
    }
}

@Composable
private fun AnimeSearchContent(
    state: AnimeSearchUiState,
    onAnimeClick: (Anime) -> Unit
) {
    val colors = JchuCatalogTheme.colors
    if (state.isLoading) CircularProgressIndicator()
    state.errorMessage?.let { Text(it, color = colors.error) }
    state.results.forEach { anime ->
        AnimeResultCard(anime = anime, onClick = { onAnimeClick(anime) })
    }
}

@Composable
private fun AnimeResultCard(
    anime: Anime,
    onClick: () -> Unit
) {
    val colors = JchuCatalogTheme.colors
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (anime.imageUrl != null) {
                AsyncImage(
                    model = anime.imageUrl,
                    contentDescription = anime.title,
                    modifier = Modifier.size(76.dp).clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier.size(76.dp).background(colors.surface, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("★", color = colors.accent)
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(anime.title, color = colors.content, fontWeight = FontWeight.Bold)
                anime.englishTitle?.takeIf { it != anime.title }?.let {
                    Text(it, color = colors.content)
                }
                Text(
                    text = anime.score?.let { "Puntuación $it" } ?: "Sin puntuación",
                    color = colors.accent
                )
            }
        }
    }
}
