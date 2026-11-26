package com.jeluchu.jchucomponents.pdf

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jeluchu.jchucomponents.pdf.resources.Res
import com.jeluchu.jchucomponents.pdf.resources.jchu_pdf_desktop_hint
import com.jeluchu.jchucomponents.pdf.resources.jchu_pdf_desktop_open
import org.jetbrains.compose.resources.stringResource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.ktor.client.HttpClient
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.Desktop
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

/**
 * Desktop (JVM) PDF reader. Pages are not drawn in the app: the document is downloaded and handed to the
 * default PDF viewer, which renders it better than any embedded renderer would.
 */
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
        JvmPdfDocument(file)
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
    val file = (document as JvmPdfDocument).file
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(Res.string.jchu_pdf_desktop_hint),
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
        Button(onClick = { runCatching { Desktop.getDesktop().open(file) } }) {
            Text(text = stringResource(Res.string.jchu_pdf_desktop_open))
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
    override val pageCount: Int = 1
    override val supportsInlineReading: Boolean = false

    // The file is a cached download that is cleaned up later; the viewer may still be reading it.
    override fun close() = Unit
}
