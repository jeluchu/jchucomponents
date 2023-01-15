/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.context

import android.Manifest
import android.app.NotificationManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmapOrNull
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.jeluchu.jchucomponents.ktx.constants.INTENT_TYPE_IMG_PNG
import com.jeluchu.jchucomponents.ktx.packageutils.buildIsQAndUp
import com.jeluchu.jchucomponents.ktx.utilities.zxing.EncodeHintType
import com.jeluchu.jchucomponents.ktx.utilities.zxing.qrcode.QRCodeWriter
import com.jeluchu.jchucomponents.ktx.utilities.zxing.qrcode.decoder.ErrorCorrectionLevel


/** ---- PERMISSIONS --------------------------------------------------------------------------- **/

fun Context.checkSelfPermissionCompat(permission: String) =
    ActivityCompat.checkSelfPermission(this, permission)

inline val Context.checkPermisionStorage: Boolean
    get() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_DENIED


/** ---- CLIPBOARD ----------------------------------------------------------------------------- **/

fun Context.addToClipboard(str: CharSequence?) {
    com.jeluchu.jchucomponents.ktx.coroutines.noCrash {
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

suspend fun Context.getImageToBitmap(
    url: String,
    force: Boolean = false,
    isHardware: Boolean = false
): Bitmap? {
    val request = ImageRequest.Builder(this).data(url).apply {
        if (force) {
            memoryCachePolicy(CachePolicy.DISABLED)
            diskCachePolicy(CachePolicy.DISABLED)
        }
        allowHardware(isHardware)
    }.build()

    return when (val result = imageLoader.execute(request)) {
        is ErrorResult -> throw result.throwable
        is SuccessResult -> result.drawable.toBitmapOrNull()
    }
}

fun Context.openPlaystoreSubscriptions(
    productId: String,
    packageName: String
) {
    runCatching {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/account/subscriptions?sku=$productId&package=$packageName")
            )
        )
    }.getOrElse {
        it.printStackTrace()
    }
}