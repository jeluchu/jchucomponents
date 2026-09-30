package com.jeluchu.composer.features.navigationcodegen.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal const val TENRAI_API_BASE_URL = "https://api.tenrai.org/v1/"

internal class TenraiAnimeService(
    private val client: HttpClient
) {
    suspend fun searchAnime(query: String): List<TenraiAnimeDto> =
        client.get("anime") {
            parameter("q", query)
            parameter("limit", 12)
        }.body<TenraiAnimeSearchResponse>().data

    suspend fun getAnime(animeId: Int): TenraiAnimeDto =
        client.get("anime/$animeId/full")
            .body<TenraiAnimeDetailsResponse>()
            .data
}

@Serializable
internal data class TenraiAnimeSearchResponse(
    val data: List<TenraiAnimeDto> = emptyList()
)

@Serializable
internal data class TenraiAnimeDetailsResponse(
    val data: TenraiAnimeDto
)

@Serializable
internal data class TenraiAnimeDto(
    @SerialName("mal_id") val id: Int,
    val title: String,
    @SerialName("title_english") val englishTitle: String? = null,
    val synopsis: String? = null,
    val score: Double? = null,
    val type: String? = null,
    val episodes: Int? = null,
    val status: String? = null,
    val year: Int? = null,
    val images: TenraiImagesDto? = null,
    val genres: List<TenraiGenreDto> = emptyList()
)

@Serializable
internal data class TenraiImagesDto(
    val jpg: TenraiImageDto? = null,
    val webp: TenraiImageDto? = null
)

@Serializable
internal data class TenraiImageDto(
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("large_image_url") val largeImageUrl: String? = null
)

@Serializable
internal data class TenraiGenreDto(
    val name: String
)
