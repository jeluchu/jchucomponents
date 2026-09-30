package com.jeluchu.composer.features.navigationcodegen.di

import com.jeluchu.composer.features.navigationcodegen.data.TENRAI_API_BASE_URL
import com.jeluchu.composer.features.navigationcodegen.data.TenraiAnimeService
import com.jeluchu.composer.features.navigationcodegen.data.TenraiAnimeRepository
import com.jeluchu.composer.features.navigationcodegen.domain.AnimeRepository
import com.jeluchu.composer.features.navigationcodegen.navigation.NavigationCodegenRoutes
import com.jeluchu.composer.features.navigationcodegen.viewmodel.AnimeDetailsViewModel
import com.jeluchu.composer.features.navigationcodegen.viewmodel.AnimeSearchViewModel
import com.jeluchu.jchucomponents.network.http.HttpClientConfiguration
import com.jeluchu.jchucomponents.network.http.createHttpClient
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val animeModule = module {
    single {
        createHttpClient(HttpClientConfiguration(baseUrl = TENRAI_API_BASE_URL))
    }
    single {
        TenraiAnimeService(get())
    }
    single<AnimeRepository> {
        TenraiAnimeRepository(get())
    }
    viewModel {
        AnimeSearchViewModel(get())
    }
    viewModel { parameters ->
        val key = parameters.get<NavigationCodegenRoutes.AnimeDetailsRoute>()
        AnimeDetailsViewModel(
            animeId = key.animeId,
            initialTitle = key.title,
            repository = get()
        )
    }
}
