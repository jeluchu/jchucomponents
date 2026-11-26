package com.jeluchu.catalog

import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.ClipEntry

@OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)
suspend fun copyToClipboard(clipboard: Clipboard, text: String): Boolean =
    runCatching { clipboard.setClipEntry(ClipEntry.withPlainText(text)) }.isSuccess || legacyCopy(text)

/** Fallback for browsers or contexts where the async Clipboard API is blocked. */
private fun legacyCopy(text: String): Boolean =
    js(
        "(function(){var t=document.createElement('textarea');t.value=text;t.style.position='fixed';t.style.opacity='0';" +
            "document.body.appendChild(t);t.select();var ok=false;try{ok=document.execCommand('copy')}catch(e){}" +
            "document.body.removeChild(t);return ok})()"
    )

fun readHash(): String = js("window.location.hash")

fun writeHash(hash: String): Unit = js("window.history.replaceState(null, '', hash)")

fun currentUrl(): String = js("window.location.href")

/** Removes the loading screen from index.html once the first frame is composed. */
fun hideLoading(): Unit = js("(function(){var l=document.getElementById('loading');if(l){l.remove()}})()")

/** Calls [onSlash] when "/" is pressed while no text input has focus. */
fun listenForSlash(onSlash: () -> Unit): Unit =
    js(
        "document.addEventListener('keydown', function(e){" +
            "var t=document.activeElement;" +
            "var typing=t&&(t.tagName==='INPUT'||t.tagName==='TEXTAREA'||t.isContentEditable);" +
            "if(e.key==='/'&&!typing&&!e.ctrlKey&&!e.metaKey&&!e.altKey){e.preventDefault();onSlash();}})"
    )
