package com.jeluchu.jchucomponents.pdf.kmp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.jeluchu.jchucomponents.ui.composables.images.jchuZoomable
import com.jeluchu.jchucomponents.ui.composables.images.rememberJchuZoomState
import io.ktor.client.HttpClient
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Closeable
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
internal actual fun rememberPdfCacheDirectory(): String = LocalContext.current.cacheDir.absolutePath

internal actual suspend fun downloadPdfDocument(
    client: HttpClient,
    url: String,
    cacheDirectory: String,
): PdfDocumentHandle = withContext(Dispatchers.IO) {
    val directory = File(cacheDirectory, "jchu-pdf-kmp").apply { mkdirs() }
    val staleBefore = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)
    directory.listFiles()
        ?.filter { it.name.startsWith("document-") && it.lastModified() < staleBefore }
        ?.forEach(File::delete)
    val file = File.createTempFile("document-", ".pdf", directory)

    try {
        val response = fetchPdfResponse(client, url)
        val channel = response.bodyAsChannel()
        FileOutputStream(file).use { output ->
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            while (true) {
                val bytesRead = channel.readAvailable(buffer)
                if (bytesRead < 0) break
                if (bytesRead == 0) continue
                output.write(buffer, 0, bytesRead)
            }
            output.flush()
        }
        check(file.length() > 0L) { "The downloaded PDF is empty." }
        AndroidPdfDocument(file).also { document ->
            if (document.pageCount == 0) {
                document.close()
                error("The PDF does not contain any pages.")
            }
        }
    } catch (cause: Throwable) {
        file.delete()
        throw cause
    }
}

@Composable
internal actual fun PdfPageContent(
    document: PdfDocumentHandle,
    pageIndex: Int,
    rotationDegrees: Int,
    contentDescription: String,
    modifier: Modifier,
) {
    val androidDocument = document as AndroidPdfDocument
    val zoomState = rememberJchuZoomState()
    LaunchedEffect(rotationDegrees) { zoomState.reset() }

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        val viewportWidth = constraints.maxWidth
        val viewportHeight = constraints.maxHeight
        var bitmap by remember(androidDocument, pageIndex, viewportWidth, viewportHeight, rotationDegrees) {
            mutableStateOf<Bitmap?>(null)
        }

        LaunchedEffect(androidDocument, pageIndex, viewportWidth, viewportHeight, rotationDegrees) {
            bitmap = androidDocument.renderPage(
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
            CircularProgressIndicator()
        } else {
            Image(
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = contentDescription,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .jchuZoomable(zoomState),
            )
        }
    }
}

@Composable
internal actual fun rememberPdfShareAction(
    chooserLabel: String,
    onShareError: () -> Unit,
): (PdfDocumentHandle) -> Unit {
    val context = LocalContext.current
    val currentOnShareError by rememberUpdatedState(onShareError)

    return remember(context) {
        { document ->
            val file = (document as? AndroidPdfDocument)?.file
            if (file == null) {
                currentOnShareError()
            } else {
                runCatching {
                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.jchucomponents.pdf.kmp.files",
                        file,
                    )
                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "application/pdf"
                        putExtra(Intent.EXTRA_STREAM, uri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    val chooser = Intent.createChooser(sendIntent, chooserLabel).apply {
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    if (context !is android.app.Activity) {
                        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(chooser)
                }.onFailure { currentOnShareError() }
            }
        }
    }
}

private class AndroidPdfDocument(
    internal val file: File,
) : PdfDocumentHandle, Closeable {
    private val lock = Any()
    private val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
    private val renderer = try {
        PdfRenderer(fileDescriptor)
    } catch (cause: Throwable) {
        fileDescriptor.close()
        throw cause
    }
    private var isClosed = false

    override val pageCount: Int
        get() = renderer.pageCount

    suspend fun renderPage(
        pageIndex: Int,
        viewportWidth: Int,
        viewportHeight: Int,
        rotationDegrees: Int,
    ): Bitmap = withContext(Dispatchers.IO) {
        synchronized(lock) {
            check(!isClosed) { "PDF document is already closed." }
            require(pageIndex in 0 until renderer.pageCount) { "PDF page index is out of bounds." }
            require(viewportWidth > 0 && viewportHeight > 0) { "PDF viewport must have a positive size." }

            val page = renderer.openPage(pageIndex)
            try {
                val rotation = ((rotationDegrees % 360) + 360) % 360
                val swapsAxes = rotation == 90 || rotation == 270
                val renderWidth = if (swapsAxes) viewportHeight else viewportWidth
                val renderHeight = if (swapsAxes) viewportWidth else viewportHeight
                val scale = min(
                    renderWidth / page.width.toFloat(),
                    renderHeight / page.height.toFloat(),
                )
                val bitmapWidth = max(1, (page.width * scale).roundToInt())
                val bitmapHeight = max(1, (page.height * scale).roundToInt())
                val bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888)
                page.render(
                    bitmap,
                    null,
                    Matrix().apply { setScale(scale, scale) },
                    PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY,
                )

                if (rotation == 0) {
                    bitmap
                } else {
                    val rotated = Bitmap.createBitmap(
                        bitmap,
                        0,
                        0,
                        bitmap.width,
                        bitmap.height,
                        Matrix().apply { postRotate(rotation.toFloat()) },
                        true,
                    )
                    if (rotated !== bitmap) bitmap.recycle()
                    rotated
                }
            } finally {
                page.close()
            }
        }
    }

    override fun close() {
        synchronized(lock) {
            if (isClosed) return
            isClosed = true
            try {
                renderer.close()
            } finally {
                fileDescriptor.close()
            }
        }
    }
}
