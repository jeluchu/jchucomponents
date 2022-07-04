/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.core.extensions.context

import android.Manifest
import android.app.NotificationManager
import android.content.*
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Context.NOTIFICATION_SERVICE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jeluchu.jchucomponents.core.extensions.coroutines.noCrash
import com.jeluchu.jchucomponents.core.extensions.intent.INTENT_TYPE_IMG_PNG
import com.jeluchu.jchucomponents.core.extensions.packageutils.buildIsQAndUp
import com.jeluchu.jchucomponents.utils.zxing.EncodeHintType
import com.jeluchu.jchucomponents.utils.zxing.qrcode.QRCodeWriter
import com.jeluchu.jchucomponents.utils.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.*


/** ---- PERMISSIONS --------------------------------------------------------------------------- **/

fun Context.checkSelfPermissionCompat(permission: String) =
    ActivityCompat.checkSelfPermission(this, permission)

inline val Context.checkPermisionStorage: Boolean
    get() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_DENIED

/** ---- BITMAPS ------------------------------------------------------------------------------- **/

fun Context.getBitmapFromVectorDrawable(
    width: Int?,
    height: Int?,
    drawableId: Int
): Bitmap? {
    val drawable = ContextCompat.getDrawable(this, drawableId)
    val bitmap = Bitmap.createBitmap(
        width ?: drawable?.intrinsicWidth ?: 1,
        height ?: drawable?.intrinsicHeight ?: 1,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable?.setBounds(0, 0, canvas.width, canvas.height)
    drawable?.draw(canvas)
    return bitmap
}

/** ---- CLIPBOARD ----------------------------------------------------------------------------- **/

fun Context.addToClipboard(str: CharSequence?) {
    noCrash {
        val clipboard =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", str)
        clipboard.setPrimaryClip(clip)
    }
}



/** ---- PRIVATE METHODS ----------------------------------------------------------------------- **/

val Context.layoutInflater
    get() = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

fun Context.getCompatDrawable(@DrawableRes drawableRes: Int): Drawable? =
    ContextCompat.getDrawable(this, drawableRes)

inline val Context.notificationManager: NotificationManager
    get() = getSystemService(NOTIFICATION_SERVICE) as NotificationManager


/** ---- CREATE QR ----------------------------------------------------------------------------- **/

fun Context.createQR(
    @DrawableRes icon: Int,
    key: String?,
    width: Int? = 768,
    height: Int? = 768
): Bitmap? {

    val qrCode: Bitmap? = null

    runCatching {
        val hints = HashMap<EncodeHintType, Any?>()
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.M
        hints[EncodeHintType.MARGIN] = 0
        val writer = QRCodeWriter()
        return writer.encode(key, width ?: 768, height ?: 768, hints, qrCode, this, icon)
    }.getOrElse {
        it.localizedMessage
        return null
    }

}



/** ---- SAVE IMAGES --------------------------------------------------------------------------- **/

fun Context.saveBitmap(
    bitmap: Bitmap,
    filename: String = System.currentTimeMillis().toString()
): Uri? {

    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
        put(MediaStore.MediaColumns.MIME_TYPE, INTENT_TYPE_IMG_PNG)
        if (buildIsQAndUp) put(
            MediaStore.MediaColumns.RELATIVE_PATH,
            Environment.DIRECTORY_PICTURES
        )
    }

    with(contentResolver) {
        val imageUri: Uri? = insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )

        return imageUri.also {
            val fileOutputStream = imageUri?.let { openOutputStream(it) }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream?.close()
        }
    }

}