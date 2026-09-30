package com.jeluchu.composer.features.navigationcodegen.domain

data class Anime(
    val id: Int,
    val title: String,
    val englishTitle: String? = null,
    val synopsis: String? = null,
    val score: Double? = null,
    val imageUrl: String? = null,
    val type: String? = null,
    val episodes: Int? = null,
    val status: String? = null,
    val year: Int? = null,
    val genres: List<String> = emptyList()
)

interface AnimeRepository {
    suspend fun searchAnime(query: String): List<Anime>

    suspend fun getAnime(animeId: Int): Anime
}
