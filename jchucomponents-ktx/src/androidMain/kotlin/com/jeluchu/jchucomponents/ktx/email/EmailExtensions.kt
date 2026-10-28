package com.jeluchu.jchucomponents.ktx.email

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.jeluchu.jchucomponents.ktx.constants.INTENT_TYPE_EMAIL

/**
 * Opens the system chooser with a prefilled email.
 *
 * [chooserTitle] should be localized by the calling app.
 */
fun Context.sendEmail(
    recipient: String,
    subject: String,
    message: String,
    chooserTitle: String,
) {
    val emailIntent =
        Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
            type = INTENT_TYPE_EMAIL
        }

    val chooserIntent = Intent.createChooser(emailIntent, chooserTitle)
    if (this !is Activity) chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    runCatching {
        startActivity(chooserIntent)
    }
}
