package com.jeluchu.composer.features.navigationcodegen.data

import com.jeluchu.composer.features.navigationcodegen.domain.Anime
import com.jeluchu.composer.features.navigationcodegen.domain.AnimeRepository

internal class TenraiAnimeRepository(
    private val service: TenraiAnimeService
) : AnimeRepository {
    override suspend fun searchAnime(query: String): List<Anime> =
        service.searchAnime(query).map(TenraiAnimeDto::toDomain)

    override suspend fun getAnime(animeId: Int): Anime =
        service.getAnime(animeId).toDomain()
}

private fun TenraiAnimeDto.toDomain() =
    Anime(
        id = id,
        title = title,
        englishTitle = englishTitle,
        synopsis = synopsis,
        score = score,
        imageUrl =
            images?.webp?.largeImageUrl
                ?: images?.jpg?.largeImageUrl
                ?: images?.webp?.imageUrl
                ?: images?.jpg?.imageUrl,
        type = type,
        episodes = episodes,
        status = status,
        year = year,
        genres = genres.map(TenraiGenreDto::name)
    )
