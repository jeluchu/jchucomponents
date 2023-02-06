/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.context.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 *
 * Author: @Jeluchu
 *
 * This class is used to show inside
 * a CustomTab the option of share the url
 *
 */
class ShareBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val url = intent.dataString
        if (url != null) {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, url)
            val chooserIntent = Intent.createChooser(
                shareIntent,
                "Compartir en:"
            )
            chooserIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(chooserIntent)
        }
    }
}