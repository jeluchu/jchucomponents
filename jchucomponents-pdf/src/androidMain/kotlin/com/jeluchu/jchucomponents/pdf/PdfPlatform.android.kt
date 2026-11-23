package com.jeluchu.jchucomponents.pdf

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
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
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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
        // Keeps showing the previous bitmap while the next one renders, so rotating or resizing
        // does not flash an empty page.
        var bitmap by remember(androidDocument, pageIndex) { mutableStateOf<Bitmap?>(null) }

        LaunchedEffect(androidDocument, pageIndex, viewportWidth, viewportHeight, rotationDegrees) {
            androidDocument.cachedPage(pageIndex, viewportWidth, viewportHeight, rotationDegrees, sharp = true)
                ?.let {
                    bitmap = it
                    return@LaunchedEffect
                }
            // A viewport-sized render first (fast), then the sharper one used for zooming.
            bitmap = androidDocument.renderPage(pageIndex, viewportWidth, viewportHeight, rotationDegrees, sharp = false)
            bitmap = androidDocument.renderPage(pageIndex, viewportWidth, viewportHeight, rotationDegrees, sharp = true)
        }

        if (bitmap == null) {
            JchuPdfLoadingIndicator()
        } else {
            LaunchedEffect(bitmap) {
                // Lets the zoom state pan within the page instead of within the whole viewport.
                bitmap?.let { zoomState.setContentSize(Size(it.width.toFloat(), it.height.toFloat())) }
            }
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

    private val renderMutex = Mutex()
    private val cache = object : LinkedHashMap<RenderKey, Bitmap>(16, 0.75f, true) {}
    private var cacheBytes = 0

    fun cachedPage(
        pageIndex: Int,
        viewportWidth: Int,
        viewportHeight: Int,
        rotationDegrees: Int,
        sharp: Boolean,
    ): Bitmap? = synchronized(cache) {
        cache[RenderKey(pageIndex, viewportWidth, viewportHeight, normalizedRotation(rotationDegrees), sharp)]
    }

    suspend fun renderPage(
        pageIndex: Int,
        viewportWidth: Int,
        viewportHeight: Int,
        rotationDegrees: Int,
        sharp: Boolean,
    ): Bitmap = withContext(Dispatchers.IO) {
        val rotation = normalizedRotation(rotationDegrees)
        val key = RenderKey(pageIndex, viewportWidth, viewportHeight, rotation, sharp)
        cachedPage(pageIndex, viewportWidth, viewportHeight, rotation, sharp)?.let { return@withContext it }

        // A page the user already swiped past is cancelled while it waits here instead of piling up.
        renderMutex.withLock {
            currentCoroutineContext().ensureActive()
            cachedPage(pageIndex, viewportWidth, viewportHeight, rotation, sharp)?.let { return@withLock it }
            renderUncached(pageIndex, viewportWidth, viewportHeight, rotation, sharp).also { rendered ->
                synchronized(cache) {
                    cache[key] = rendered
                    cacheBytes += rendered.byteCount
                    val iterator = cache.entries.iterator()
                    while (cacheBytes > MAX_CACHE_BYTES && iterator.hasNext()) {
                        val eldest = iterator.next()
                        if (eldest.value !== rendered) {
                            cacheBytes -= eldest.value.byteCount
                            iterator.remove()
                        }
                    }
                }
            }
        }
    }

    private fun renderUncached(
        pageIndex: Int,
        viewportWidth: Int,
        viewportHeight: Int,
        rotation: Int,
        sharp: Boolean,
    ): Bitmap = synchronized(lock) {
        check(!isClosed) { "PDF document is already closed." }
        require(pageIndex in 0 until renderer.pageCount) { "PDF page index is out of bounds." }
        require(viewportWidth > 0 && viewportHeight > 0) { "PDF viewport must have a positive size." }

        val page = renderer.openPage(pageIndex)
        try {
            val swapsAxes = rotation == 90 || rotation == 270
            val renderWidth = if (swapsAxes) viewportHeight else viewportWidth
            val renderHeight = if (swapsAxes) viewportWidth else viewportHeight
            val fitScale = min(
                renderWidth / page.width.toFloat(),
                renderHeight / page.height.toFloat(),
            )
            // The sharp render goes above the viewport size so the page stays crisp when zoomed,
            // without letting a huge page allocate an oversized bitmap.
            val scale = if (sharp) {
                (fitScale * ZOOM_RENDER_FACTOR)
                    .coerceAtMost(MAX_RENDER_SIDE / max(page.width, page.height).toFloat())
                    .coerceAtLeast(fitScale)
            } else {
                fitScale
            }
            val bitmapWidth = max(1, (page.width * scale).roundToInt())
            val bitmapHeight = max(1, (page.height * scale).roundToInt())
            val bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888)
            // PdfRenderer draws on a transparent bitmap: pages without their own background would
            // show dark text over the dark viewer.
            bitmap.eraseColor(android.graphics.Color.WHITE)
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

private data class RenderKey(
    val pageIndex: Int,
    val viewportWidth: Int,
    val viewportHeight: Int,
    val rotation: Int,
    val sharp: Boolean,
)

private fun normalizedRotation(degrees: Int): Int = ((degrees % 360) + 360) % 360

private const val ZOOM_RENDER_FACTOR = 2f
private const val MAX_RENDER_SIDE = 4096f
private const val MAX_CACHE_BYTES = 96 * 1024 * 1024
