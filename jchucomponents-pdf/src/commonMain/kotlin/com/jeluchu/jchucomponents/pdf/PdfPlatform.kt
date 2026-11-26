package com.jeluchu.jchucomponents.pdf

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.ktor.client.HttpClient

internal interface PdfDocumentHandle {
    val pageCount: Int

    /** False where pages cannot be drawn in the app (desktop): the reader then offers to open the file instead. */
    val supportsInlineReading: Boolean get() = true

    fun close()
}

@Composable
internal expect fun rememberPdfCacheDirectory(): String

internal expect suspend fun downloadPdfDocument(
    client: HttpClient,
    url: String,
    cacheDirectory: String,
): PdfDocumentHandle

@Composable
internal expect fun PdfPageContent(
    document: PdfDocumentHandle,
    pageIndex: Int,
    rotationDegrees: Int,
    contentDescription: String,
    modifier: Modifier,
)

@Composable
internal expect fun rememberPdfShareAction(
    chooserLabel: String,
    onShareError: () -> Unit,
): (PdfDocumentHandle) -> Unit
