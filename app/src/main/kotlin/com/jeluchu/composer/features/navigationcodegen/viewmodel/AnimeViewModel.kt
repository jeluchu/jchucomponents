package com.jeluchu.composer.features.navigationcodegen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeluchu.composer.features.navigationcodegen.domain.Anime
import com.jeluchu.composer.features.navigationcodegen.domain.AnimeRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal data class AnimeSearchUiState(
    val query: String = "Frieren",
    val isLoading: Boolean = false,
    val results: List<Anime> = emptyList(),
    val errorMessage: String? = null
)

internal class AnimeSearchViewModel(
    private val repository: AnimeRepository
) : ViewModel() {
    private val mutableState = MutableStateFlow(AnimeSearchUiState())
    val state: StateFlow<AnimeSearchUiState> = mutableState.asStateFlow()
    private var searchJob: Job? = null

    init {
        search()
    }

    fun onQueryChanged(query: String) {
        mutableState.update { it.copy(query = query) }
    }

    fun search() {
        val query = mutableState.value.query.trim()
        if (query.isBlank()) {
            mutableState.update { it.copy(errorMessage = "Escribe un título para buscar.") }
            return
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            mutableState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val results = repository.searchAnime(query)
                mutableState.update {
                    it.copy(
                        isLoading = false,
                        results = results,
                        errorMessage = if (results.isEmpty()) "No se encontraron resultados." else null
                    )
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                mutableState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "No se pudo consultar Tenrai."
                    )
                }
            }
        }
    }
}

internal data class AnimeDetailsUiState(
    val anime: Anime,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

internal class AnimeDetailsViewModel(
    animeId: Int,
    initialTitle: String,
    private val repository: AnimeRepository
) : ViewModel() {
    private val mutableState = MutableStateFlow(
        AnimeDetailsUiState(anime = Anime(id = animeId, title = initialTitle))
    )
    val state: StateFlow<AnimeDetailsUiState> = mutableState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                mutableState.update {
                    it.copy(anime = repository.getAnime(animeId), isLoading = false)
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                mutableState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "No se pudo cargar el detalle."
                    )
                }
            }
        }
    }
}
