package com.jeluchu.jchucomponentscompose.utils.broadcast

import android.content.*
import android.widget.Toast
import com.jeluchu.jchucomponentscompose.core.extensions.context.addToClipboard

class CustomTabsCopyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        val url = intent.dataString
        if (url != null) {
            context?.addToClipboard(url)
            Toast.makeText(
                context,
                "Enlace copiado al portapapeles",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
