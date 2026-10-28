package com.jeluchu.jchucomponents.pdf.kmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.viewinterop.UIKitView
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSDate
import platform.Foundation.NSFileManager
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUUID
import platform.Foundation.dataWithBytes
import platform.Foundation.timeIntervalSince1970
import platform.Foundation.writeToFile
import platform.PDFKit.kPDFDisplayDirectionVertical
import platform.PDFKit.kPDFDisplaySinglePage
import platform.PDFKit.PDFDocument
import platform.PDFKit.PDFPage
import platform.PDFKit.PDFView
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIColor
import platform.UIKit.UIViewController
import platform.UIKit.popoverPresentationController

@Composable
internal actual fun rememberPdfCacheDirectory(): String = NSTemporaryDirectory().let { path ->
    if (path.endsWith('/')) path else "$path/"
}

@OptIn(ExperimentalForeignApi::class)
internal actual suspend fun downloadPdfDocument(
    client: HttpClient,
    url: String,
    cacheDirectory: String,
): PdfDocumentHandle {
    cleanupStalePdfFiles(cacheDirectory)
    val response = fetchPdfResponse(client, url)
    val bytes = response.body<ByteArray>()
    check(bytes.isNotEmpty()) { "The downloaded PDF is empty." }

    val data = bytes.usePinned { pinned ->
        NSData.dataWithBytes(
            bytes = pinned.addressOf(0),
            length = bytes.size.toULong(),
        )
    }
    val filePath = "${cacheDirectory}jchu-pdf-${NSDate().timeIntervalSince1970.toLong()}-${NSUUID().UUIDString}.pdf"
    check(data.writeToFile(filePath, atomically = true)) { "The PDF could not be cached." }

    val fileUrl = NSURL.fileURLWithPath(filePath)
    val document = PDFDocument(uRL = fileUrl)
    check(document.pageCount.toInt() > 0) { "The PDF does not contain any pages." }

    return IosPdfDocument(fileUrl, document)
}

@Composable
internal actual fun PdfPageContent(
    document: PdfDocumentHandle,
    pageIndex: Int,
    rotationDegrees: Int,
    contentDescription: String,
    modifier: Modifier,
) {
    val iosDocument = document as IosPdfDocument
    val page = iosDocument.document.pageAtIndex(pageIndex.toULong()) ?: return
    var appliedRotation by remember(iosDocument, pageIndex) {
        mutableIntStateOf(0)
    }
    val rotationDelta = rotationDegrees - appliedRotation
    if (rotationDelta != 0) {
        page.rotation = ((page.rotation.toInt() + rotationDelta + 360) % 360).toLong()
        appliedRotation = rotationDegrees
    }

    UIKitView(
        factory = {
            PDFView().apply {
                backgroundColor = UIColor.blackColor
                this.document = iosDocument.document
                autoScales = true
                displayMode = kPDFDisplaySinglePage
                displayDirection = kPDFDisplayDirectionVertical
                goToPage(page)
            }
        },
        modifier = modifier.semantics { this.contentDescription = contentDescription },
        update = { pdfView ->
            if (pdfView.document !== iosDocument.document) {
                pdfView.document = iosDocument.document
            }
            pdfView.goToPage(page)
        },
    )
}

@Composable
@OptIn(ExperimentalForeignApi::class)
internal actual fun rememberPdfShareAction(
    chooserLabel: String,
    onShareError: () -> Unit,
): (PdfDocumentHandle) -> Unit {
    val currentOnShareError by rememberUpdatedState(onShareError)

    return remember {
        { document ->
            val iosDocument = document as? IosPdfDocument
            val presenter = currentViewController()
            if (iosDocument == null || presenter == null) {
                currentOnShareError()
            } else {
                val sharePath = "${NSTemporaryDirectory()}jchu-pdf-${NSDate().timeIntervalSince1970.toLong()}-${NSUUID().UUIDString}.pdf"
                if (!iosDocument.document.writeToFile(sharePath)) {
                    currentOnShareError()
                } else {
                    val activityController = UIActivityViewController(
                        activityItems = listOf(NSURL.fileURLWithPath(sharePath)),
                        applicationActivities = null,
                    )
                    val popover = (activityController as UIViewController).popoverPresentationController
                    popover?.sourceView = presenter.view
                    popover?.sourceRect = CGRectMake(0.0, 0.0, 1.0, 1.0)
                    presenter.presentViewController(
                        viewControllerToPresent = activityController,
                        animated = true,
                        completion = null,
                    )
                }
            }
        }
    }
}

private class IosPdfDocument(
    val fileUrl: NSURL,
    val document: PDFDocument,
) : PdfDocumentHandle {
    override val pageCount: Int
        get() = document.pageCount.toInt()

    override fun close() = Unit
}

private fun currentViewController(): UIViewController? {
    var presenter = UIApplication.sharedApplication.keyWindow?.rootViewController ?: return null
    while (true) {
        val next = presenter.presentedViewController ?: break
        presenter = next
    }
    return presenter
}

@OptIn(ExperimentalForeignApi::class)
private fun cleanupStalePdfFiles(cacheDirectory: String) {
    val fileManager = NSFileManager.defaultManager
    val files = fileManager.contentsOfDirectoryAtPath(cacheDirectory, error = null) ?: return
    val staleBefore = NSDate().timeIntervalSince1970.toLong() - 24 * 60 * 60

    files.map { it.toString() }.filter { it.startsWith("jchu-pdf-") }.forEach { fileName ->
        val createdAt = fileName
            .removePrefix("jchu-pdf-")
            .substringBefore('-')
            .toLongOrNull()
        if (createdAt != null && createdAt < staleBefore) {
            fileManager.removeItemAtPath("$cacheDirectory$fileName", error = null)
        }
    }
}
