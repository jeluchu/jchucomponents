/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.context.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.jeluchu.jchucomponents.ktx.context.addToClipboard

/**
 *
 * Author: @Jeluchu
 *
 * This class is used to show inside
 * a CustomTab the option of copying the url
 *
 */

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
