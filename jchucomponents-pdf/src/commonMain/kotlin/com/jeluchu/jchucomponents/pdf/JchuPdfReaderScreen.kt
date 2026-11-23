package com.jeluchu.jchucomponents.pdf

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.RotateLeft
import androidx.compose.material.icons.automirrored.filled.RotateRight
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.navigation3.BackStackBack
import com.jeluchu.jchucomponents.navigation3.Screen
import com.jeluchu.jchucomponents.pdf.resources.Res
import com.jeluchu.jchucomponents.pdf.resources.jchu_pdf_back
import com.jeluchu.jchucomponents.pdf.resources.jchu_pdf_error
import com.jeluchu.jchucomponents.pdf.resources.jchu_pdf_error_unknown
import com.jeluchu.jchucomponents.pdf.resources.jchu_pdf_next_page
import com.jeluchu.jchucomponents.pdf.resources.jchu_pdf_page_description
import com.jeluchu.jchucomponents.pdf.resources.jchu_pdf_page_indicator
import com.jeluchu.jchucomponents.pdf.resources.jchu_pdf_previous_page
import com.jeluchu.jchucomponents.pdf.resources.jchu_pdf_retry
import com.jeluchu.jchucomponents.pdf.resources.jchu_pdf_rotate_left
import com.jeluchu.jchucomponents.pdf.resources.jchu_pdf_rotate_right
import com.jeluchu.jchucomponents.pdf.resources.jchu_pdf_share
import com.jeluchu.jchucomponents.pdf.resources.jchu_pdf_share_dismiss
import com.jeluchu.jchucomponents.pdf.resources.jchu_pdf_share_error
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
    val pagerState = rememberPagerState(pageCount = { document.pageCount })
    val rotations = remember(document) { mutableStateMapOf<Int, Int>() }
    val currentPage = pagerState.currentPage

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PdfViewerBackground),
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            beyondViewportPageCount = 2,
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

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            PdfControlButton(
                modifier = Modifier.align(Alignment.CenterStart),
                icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = stringResource(Res.string.jchu_pdf_back),
                onClick = onBack,
            )
            Text(
                text = stringResource(
                    Res.string.jchu_pdf_page_indicator,
                    currentPage + 1,
                    document.pageCount,
                ),
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(PdfControlShape)
                    .background(PdfControlBackground)
                    .padding(horizontal = 24.dp, vertical = 14.dp),
                color = PdfControlContent,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            PdfControlButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                icon = Icons.Default.Share,
                contentDescription = stringResource(Res.string.jchu_pdf_share),
                onClick = onShare,
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal))
                .padding(horizontal = 16.dp, vertical = 16.dp),
        ) {
            if (currentPage > 0) {
                PdfControlButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(Res.string.jchu_pdf_previous_page),
                    onClick = { scope.launch { pagerState.animateScrollToPage(currentPage - 1) } },
                )
            }
            Row(
                modifier = Modifier.align(Alignment.Center),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                PdfControlButton(
                    icon = Icons.AutoMirrored.Filled.RotateLeft,
                    contentDescription = stringResource(Res.string.jchu_pdf_rotate_left),
                    onClick = { rotations[currentPage] = (rotations[currentPage] ?: 0) - 90 },
                )
                PdfControlButton(
                    icon = Icons.AutoMirrored.Filled.RotateRight,
                    contentDescription = stringResource(Res.string.jchu_pdf_rotate_right),
                    onClick = { rotations[currentPage] = (rotations[currentPage] ?: 0) + 90 },
                )
            }
            if (currentPage < document.pageCount - 1) {
                PdfControlButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    icon = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = stringResource(Res.string.jchu_pdf_next_page),
                    onClick = { scope.launch { pagerState.animateScrollToPage(currentPage + 1) } },
                )
            }
        }
    }
}

/** The Material 3 Expressive morphing loader, white over the dark viewer. */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun JchuPdfLoadingIndicator(modifier: Modifier = Modifier) {
    LoadingIndicator(modifier = modifier, color = PdfControlContent)
}

private val PdfViewerBackground = Color(0xFF3F3F3F)
private val PdfControlBackground = Color.Black.copy(alpha = 0.4f)
private val PdfControlContent = Color.White
private val PdfControlShape = RoundedCornerShape(18.dp)

@Composable
private fun PdfControlButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(52.dp)
            .clip(PdfControlShape)
            .background(PdfControlBackground)
            .clickable(role = Role.Button, onClickLabel = contentDescription, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = PdfControlContent,
        )
    }
}

@Composable
private fun JchuPdfLoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PdfViewerBackground),
        contentAlignment = Alignment.Center,
    ) {
        JchuPdfLoadingIndicator()
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
