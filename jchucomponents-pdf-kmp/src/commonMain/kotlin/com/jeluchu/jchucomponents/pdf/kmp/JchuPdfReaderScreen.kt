package com.jeluchu.jchucomponents.pdf.kmp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.RotateLeft
import androidx.compose.material.icons.automirrored.filled.RotateRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.navigation3.BackStackBack
import com.jeluchu.jchucomponents.navigation3.Screen
import com.jeluchu.jchucomponents.pdf.kmp.resources.Res
import com.jeluchu.jchucomponents.pdf.kmp.resources.jchu_pdf_back
import com.jeluchu.jchucomponents.pdf.kmp.resources.jchu_pdf_error
import com.jeluchu.jchucomponents.pdf.kmp.resources.jchu_pdf_error_unknown
import com.jeluchu.jchucomponents.pdf.kmp.resources.jchu_pdf_next_page
import com.jeluchu.jchucomponents.pdf.kmp.resources.jchu_pdf_page_description
import com.jeluchu.jchucomponents.pdf.kmp.resources.jchu_pdf_page_indicator
import com.jeluchu.jchucomponents.pdf.kmp.resources.jchu_pdf_previous_page
import com.jeluchu.jchucomponents.pdf.kmp.resources.jchu_pdf_retry
import com.jeluchu.jchucomponents.pdf.kmp.resources.jchu_pdf_rotate_left
import com.jeluchu.jchucomponents.pdf.kmp.resources.jchu_pdf_rotate_right
import com.jeluchu.jchucomponents.pdf.kmp.resources.jchu_pdf_share
import com.jeluchu.jchucomponents.pdf.kmp.resources.jchu_pdf_share_dismiss
import com.jeluchu.jchucomponents.pdf.kmp.resources.jchu_pdf_share_error
import com.jeluchu.jchucomponents.ui.composables.structures.JchuRemoteScreenContent
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

/** Opens and displays a remote PDF in the shared Compose reader. */
@Screen(graph = "PdfReaderKmp", route = "Reader")
@Composable
fun JchuPdfReaderScreen(
    url: String,
    @BackStackBack onBack: () -> Unit,
) {
    val cacheDirectory = rememberPdfCacheDirectory()
    val client = remember { createPdfHttpClient() }
    var document by remember(url) { mutableStateOf<PdfDocumentHandle?>(null) }
    var error by remember(url) { mutableStateOf<String?>(null) }
    var isLoading by remember(url) { mutableStateOf(true) }
    var retryToken by remember(url) { mutableIntStateOf(0) }
    var showShareError by remember { mutableStateOf(false) }
    val sharePdf = rememberPdfShareAction(
        chooserLabel = stringResource(Res.string.jchu_pdf_share),
        onShareError = { showShareError = true },
    )

    LaunchedEffect(url, retryToken, cacheDirectory, client) {
        document = null
        error = null
        isLoading = true
        try {
            document = downloadPdfDocument(client, url, cacheDirectory)
        } catch (cause: CancellationException) {
            throw cause
        } catch (cause: Exception) {
            error = cause.message?.takeIf(String::isNotBlank)
        } finally {
            isLoading = false
        }
    }

    DisposableEffect(document) {
        val openedDocument = document
        onDispose { openedDocument?.close() }
    }

    DisposableEffect(client) {
        onDispose { client.close() }
    }

    JchuRemoteScreenContent(
        data = document,
        isLoading = isLoading,
        error = error,
        onLoading = { JchuPdfLoadingContent() },
        onSuccess = { loadedDocument ->
            JchuPdfDocumentContent(
                document = loadedDocument,
                onBack = onBack,
                onShare = { sharePdf(loadedDocument) },
            )
        },
        onFailure = { message ->
            JchuPdfErrorContent(
                message = message ?: stringResource(Res.string.jchu_pdf_error_unknown),
                onRetry = { retryToken += 1 },
            )
        },
    )

    if (showShareError) {
        AlertDialog(
            onDismissRequest = { showShareError = false },
            title = { Text(stringResource(Res.string.jchu_pdf_share_error)) },
            confirmButton = {
                TextButton(onClick = { showShareError = false }) {
                    Text(stringResource(Res.string.jchu_pdf_share_dismiss))
                }
            },
        )
    }
}

@Composable
private fun JchuPdfDocumentContent(
    document: PdfDocumentHandle,
    onBack: () -> Unit,
    onShare: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val colors = MaterialTheme.colorScheme
    val pagerState = rememberPagerState(pageCount = { document.pageCount })
    val rotations = remember(document) { mutableStateMapOf<Int, Int>() }
    val currentPage = pagerState.currentPage

    Scaffold(
        containerColor = colors.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.surface)
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(Res.string.jchu_pdf_back),
                        tint = colors.onSurface,
                    )
                }
                Text(
                    text = stringResource(
                        Res.string.jchu_pdf_page_indicator,
                        currentPage + 1,
                        document.pageCount,
                    ),
                    modifier = Modifier.weight(1f),
                    color = colors.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
                IconButton(onClick = onShare) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = stringResource(Res.string.jchu_pdf_share),
                        tint = colors.onSurface,
                    )
                }
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.surfaceVariant)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    enabled = currentPage > 0,
                    onClick = {
                        scope.launch { pagerState.animateScrollToPage(currentPage - 1) }
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = stringResource(Res.string.jchu_pdf_previous_page),
                        tint = colors.onSurfaceVariant,
                    )
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { rotations[currentPage] = (rotations[currentPage] ?: 0) - 90 }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.RotateLeft,
                        contentDescription = stringResource(Res.string.jchu_pdf_rotate_left),
                        tint = colors.onSurfaceVariant,
                    )
                }
                IconButton(onClick = { rotations[currentPage] = (rotations[currentPage] ?: 0) + 90 }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.RotateRight,
                        contentDescription = stringResource(Res.string.jchu_pdf_rotate_right),
                        tint = colors.onSurfaceVariant,
                    )
                }
                Spacer(Modifier.weight(1f))
                IconButton(
                    enabled = currentPage < document.pageCount - 1,
                    onClick = {
                        scope.launch { pagerState.animateScrollToPage(currentPage + 1) }
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = stringResource(Res.string.jchu_pdf_next_page),
                        tint = colors.onSurfaceVariant,
                    )
                }
            }
        },
    ) { contentPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            beyondViewportPageCount = 1,
        ) { pageIndex ->
            PdfPageContent(
                document = document,
                pageIndex = pageIndex,
                rotationDegrees = rotations[pageIndex] ?: 0,
                contentDescription = stringResource(
                    Res.string.jchu_pdf_page_description,
                    pageIndex + 1,
                ),
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun JchuPdfLoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun JchuPdfErrorContent(
    message: String,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(Res.string.jchu_pdf_error, message),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = onRetry,
        ) {
            Text(stringResource(Res.string.jchu_pdf_retry))
        }
    }
}
