package com.jeluchu.jchucomponents.pdf.kmp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.ktor.client.HttpClient

internal interface PdfDocumentHandle {
    val pageCount: Int

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
