package com.jeluchu.jchucomponents.pdf.kmp

import com.jeluchu.jchucomponents.network.http.HttpClientConfiguration
import com.jeluchu.jchucomponents.network.http.createHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders

internal fun createPdfHttpClient(): HttpClient =
    createHttpClient(
        HttpClientConfiguration(
            enableCache = false,
            expectSuccess = true,
        ),
    )

internal suspend fun fetchPdfResponse(client: HttpClient, url: String): HttpResponse {
    require(url.isNotBlank()) { "PDF URL must not be blank." }

    return client.get(url) {
        headers {
            remove(HttpHeaders.Accept)
            remove(HttpHeaders.ContentType)
            append(HttpHeaders.Accept, "application/pdf")
        }
    }
}
