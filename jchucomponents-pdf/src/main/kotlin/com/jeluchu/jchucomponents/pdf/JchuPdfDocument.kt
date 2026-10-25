package com.jeluchu.jchucomponents.pdf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import com.jeluchu.jchucomponents.network.http.HttpClientConfiguration
import com.jeluchu.jchucomponents.network.http.createHttpClient
import com.jeluchu.jchucomponents.network.utils.saveResponseBodyToFile
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Closeable
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

internal suspend fun downloadPdfDocument(
    context: Context,
    client: HttpClient,
    url: String,
    onProgress: (Long) -> Unit
): JchuPdfDocument = withContext(Dispatchers.IO) {
    require(url.isNotBlank()) { "PDF URL must not be blank." }

    val cacheDirectory = File(context.cacheDir, "jchu-pdf").apply { mkdirs() }
    val staleBefore = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)
    cacheDirectory.listFiles()
        ?.filter { it.name.startsWith("document-") && it.lastModified() < staleBefore }
        ?.forEach(File::delete)
    val file = File.createTempFile("document-", ".pdf", cacheDirectory)

    try {
        val response = client.get(url) {
            headers {
                remove(HttpHeaders.Accept)
                remove(HttpHeaders.ContentType)
                append(HttpHeaders.Accept, "application/pdf")
            }
        }
        saveResponseBodyToFile(file.absolutePath, response, onProgress)
        check(file.length() > 0L) { "The downloaded PDF is empty." }
        JchuPdfDocument(file).also { document ->
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

internal class JchuPdfDocument(
    internal val file: File
) : Closeable {
    private val lock = Any()
    private val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
    private val renderer = try {
        PdfRenderer(fileDescriptor)
    } catch (cause: Throwable) {
        fileDescriptor.close()
        throw cause
    }
    private var isClosed = false

    val pageCount: Int
        get() = renderer.pageCount

    suspend fun renderPage(
        pageIndex: Int,
        viewportWidth: Int,
        viewportHeight: Int,
        rotationDegrees: Int
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
                    renderHeight / page.height.toFloat()
                )
                val bitmapWidth = max(1, (page.width * scale).roundToInt())
                val bitmapHeight = max(1, (page.height * scale).roundToInt())
                val bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888)
                page.render(
                    bitmap,
                    null,
                    Matrix().apply { setScale(scale, scale) },
                    PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
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
                        true
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

internal fun createPdfHttpClient(): HttpClient =
    createHttpClient(
        HttpClientConfiguration(
            enableCache = false,
            expectSuccess = true
        )
    )
