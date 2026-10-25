package com.jeluchu.jchucomponents.pdf

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.RotateLeft
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.jeluchu.jchucomponents.navigation3.BackStackBack
import com.jeluchu.jchucomponents.navigation3.Screen
import com.jeluchu.jchucomponents.ui.composables.images.jchuZoomable
import com.jeluchu.jchucomponents.ui.composables.images.rememberJchuZoomState
import com.jeluchu.jchucomponents.ui.composables.structures.JchuRemoteScreenContent
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

/** Opens and displays a PDF from a remote URL. */
@Screen(graph = "PdfReader", route = "Reader")
@Composable
fun JchuPdfReaderScreen(
    url: String,
    @BackStackBack onBack: () -> Unit,
) {
    val context = LocalContext.current
    var document by remember(url) { mutableStateOf<JchuPdfDocument?>(null) }
    var error by remember(url) { mutableStateOf<String?>(null) }
    var isLoading by remember(url) { mutableStateOf(true) }
    var retryToken by remember(url) { mutableIntStateOf(0) }

    LaunchedEffect(url, retryToken) {
        document = null
        error = null
        isLoading = true

        val client = createPdfHttpClient()
        try {
            document = downloadPdfDocument(context, client, url) { }
        } catch (cause: CancellationException) {
            throw cause
        } catch (cause: Exception) {
            error = cause.localizedMessage ?: context.getString(R.string.jchu_pdf_error_unknown)
        } finally {
            client.close()
            isLoading = false
        }
    }

    DisposableEffect(document) {
        val openedDocument = document
        onDispose { openedDocument?.close() }
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
            )
        },
        onFailure = { message ->
            JchuPdfErrorContent(
                message = message ?: stringResource(R.string.jchu_pdf_error_unknown),
                onRetry = { retryToken += 1 },
            )
        },
    )
}

@Composable
private fun JchuPdfDocumentContent(
    document: JchuPdfDocument,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
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
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.jchu_pdf_back),
                        tint = colors.onSurface,
                    )
                }
                Text(
                    text = stringResource(
                        R.string.jchu_pdf_page_indicator,
                        currentPage + 1,
                        document.pageCount,
                    ),
                    modifier = Modifier.weight(1f),
                    color = colors.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
                IconButton(onClick = { context.sharePdf(document.file) }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = stringResource(R.string.jchu_pdf_share),
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
                        contentDescription = stringResource(R.string.jchu_pdf_previous_page),
                        tint = colors.onSurfaceVariant,
                    )
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { rotations[currentPage] = (rotations[currentPage] ?: 0) - 90 }) {
                    Icon(
                        imageVector = Icons.Default.RotateLeft,
                        contentDescription = stringResource(R.string.jchu_pdf_rotate_left),
                        tint = colors.onSurfaceVariant,
                    )
                }
                IconButton(onClick = { rotations[currentPage] = (rotations[currentPage] ?: 0) + 90 }) {
                    Icon(
                        imageVector = Icons.Default.RotateRight,
                        contentDescription = stringResource(R.string.jchu_pdf_rotate_right),
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
                        contentDescription = stringResource(R.string.jchu_pdf_next_page),
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
            JchuPdfPage(
                document = document,
                pageIndex = pageIndex,
                rotationDegrees = rotations[pageIndex] ?: 0,
            )
        }
    }
}

@Composable
private fun JchuPdfPage(
    document: JchuPdfDocument,
    pageIndex: Int,
    rotationDegrees: Int,
) {
    val zoomState = rememberJchuZoomState()
    LaunchedEffect(rotationDegrees) { zoomState.reset() }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        val viewportWidth = constraints.maxWidth
        val viewportHeight = constraints.maxHeight
        var bitmap by remember(document, pageIndex, viewportWidth, viewportHeight, rotationDegrees) {
            mutableStateOf<Bitmap?>(null)
        }

        LaunchedEffect(document, pageIndex, viewportWidth, viewportHeight, rotationDegrees) {
            bitmap = document.renderPage(
                pageIndex = pageIndex,
                viewportWidth = viewportWidth,
                viewportHeight = viewportHeight,
                rotationDegrees = rotationDegrees,
            )
        }

        DisposableEffect(bitmap) {
            val renderedBitmap = bitmap
            onDispose { renderedBitmap?.recycle() }
        }

        if (bitmap == null) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        } else {
            Image(
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = stringResource(R.string.jchu_pdf_page_description, pageIndex + 1),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .jchuZoomable(zoomState),
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
            text = stringResource(R.string.jchu_pdf_error, message),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = onRetry,
        ) {
            Text(stringResource(R.string.jchu_pdf_retry))
        }
    }
}

private fun Context.sharePdf(file: java.io.File) {
    runCatching {
        val uri = FileProvider.getUriForFile(
            this,
            "$packageName.jchucomponents.pdf.files",
            file,
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(intent, getString(R.string.jchu_pdf_share_chooser)))
    }.onFailure {
        Toast.makeText(this, R.string.jchu_pdf_share_error, Toast.LENGTH_LONG).show()
    }
}
