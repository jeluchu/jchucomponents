package com.jeluchu.jchucomponents.pdf

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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.jeluchu.jchucomponents.ui.composables.images.jchuZoomable
import com.jeluchu.jchucomponents.ui.composables.images.rememberJchuZoomState
import io.ktor.client.HttpClient
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.apache.pdfbox.Loader
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.ImageType
import org.apache.pdfbox.rendering.PDFRenderer
import java.awt.Desktop
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.math.min

/** Desktop (JVM) PDF reader backed by Apache PDFBox. */
@Composable
internal actual fun rememberPdfCacheDirectory(): String = System.getProperty("java.io.tmpdir").orEmpty()

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
        val channel = fetchPdfResponse(client, url).bodyAsChannel()
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
        JvmPdfDocument(file).also { document ->
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
    val jvmDocument = document as JvmPdfDocument
    val zoomState = rememberJchuZoomState()
    LaunchedEffect(rotationDegrees) { zoomState.reset() }

    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {
        val viewportWidth = constraints.maxWidth
        val viewportHeight = constraints.maxHeight
        // Keeps the previous bitmap on screen while the next one renders, so resizing or rotating
        // does not flash an empty page.
        var bitmap by remember(jvmDocument, pageIndex) { mutableStateOf<ImageBitmap?>(null) }

        LaunchedEffect(jvmDocument, pageIndex, viewportWidth, viewportHeight, rotationDegrees) {
            bitmap = jvmDocument.renderPage(pageIndex, viewportWidth, viewportHeight, rotationDegrees, sharp = false)
            bitmap = jvmDocument.renderPage(pageIndex, viewportWidth, viewportHeight, rotationDegrees, sharp = true)
        }

        val current = bitmap
        if (current == null) {
            JchuPdfLoadingIndicator()
        } else {
            LaunchedEffect(current) {
                zoomState.setContentSize(Size(current.width.toFloat(), current.height.toFloat()))
            }
            Image(
                bitmap = current,
                contentDescription = contentDescription,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize().jchuZoomable(zoomState),
            )
        }
    }
}

/** There is no share sheet on desktop: "share" opens the PDF in the default viewer, from where it can be saved or sent. */
@Composable
internal actual fun rememberPdfShareAction(
    chooserLabel: String,
    onShareError: () -> Unit,
): (PdfDocumentHandle) -> Unit {
    val currentOnShareError by rememberUpdatedState(onShareError)
    return remember {
        { document ->
            val file = (document as? JvmPdfDocument)?.file
            val opened = file != null && runCatching {
                check(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN))
                Desktop.getDesktop().open(file)
            }.isSuccess
            if (!opened) currentOnShareError()
        }
    }
}

private class JvmPdfDocument(val file: File) : PdfDocumentHandle {
    private val document: PDDocument = Loader.loadPDF(file)
    private val renderer = PDFRenderer(document)
    private val renderMutex = Mutex()
    private var isClosed = false

    override val pageCount: Int
        get() = document.numberOfPages

    suspend fun renderPage(
        pageIndex: Int,
        viewportWidth: Int,
        viewportHeight: Int,
        rotationDegrees: Int,
        sharp: Boolean,
    ): ImageBitmap? = withContext(Dispatchers.Default) {
        renderMutex.withLock {
            if (isClosed || pageIndex !in 0 until pageCount) return@withLock null
            val box = document.getPage(pageIndex).cropBox
            val rotated = rotationDegrees % 180 != 0
            val pageWidth = if (rotated) box.height else box.width
            val pageHeight = if (rotated) box.width else box.height
            val fit = min(
                max(viewportWidth, 1) / pageWidth,
                max(viewportHeight, 1) / pageHeight,
            )
            val scale = (fit * if (sharp) SHARP_FACTOR else 1f).coerceIn(MIN_SCALE, MAX_SCALE)
            val rendered = renderer.renderImage(pageIndex, scale, ImageType.RGB)
            rotate(rendered, rotationDegrees).toComposeImageBitmap()
        }
    }

    private fun rotate(image: BufferedImage, degrees: Int): BufferedImage {
        val normalized = ((degrees % 360) + 360) % 360
        if (normalized == 0) return image
        val radians = Math.toRadians(normalized.toDouble())
        val swap = normalized % 180 != 0
        val target = BufferedImage(
            if (swap) image.height else image.width,
            if (swap) image.width else image.height,
            BufferedImage.TYPE_INT_RGB,
        )
        val transform = AffineTransform().apply {
            translate(target.width / 2.0, target.height / 2.0)
            rotate(radians)
            translate(-image.width / 2.0, -image.height / 2.0)
        }
        val graphics = target.createGraphics()
        graphics.drawImage(image, AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR), 0, 0)
        graphics.dispose()
        return target
    }

    override fun close() {
        if (isClosed) return
        isClosed = true
        runCatching { document.close() }
    }

    private companion object {
        const val SHARP_FACTOR = 2f
        const val MIN_SCALE = 0.25f
        const val MAX_SCALE = 6f
    }
}
