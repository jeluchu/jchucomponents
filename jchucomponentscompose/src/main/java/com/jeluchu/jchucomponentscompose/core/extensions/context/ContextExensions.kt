package com.jeluchu.jchucomponentscompose.core.extensions.context

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Context.NOTIFICATION_SERVICE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Environment
import android.provider.Browser
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jeluchu.jchucomponentscompose.R
import com.jeluchu.jchucomponentscompose.core.extensions.coroutines.noCrash
import com.jeluchu.jchucomponentscompose.core.extensions.intent.INTENT_TYPE_IMG_PNG
import com.jeluchu.jchucomponentscompose.core.extensions.intent.INTENT_TYPE_TEXT
import com.jeluchu.jchucomponentscompose.core.extensions.packageutils.*
import com.jeluchu.jchucomponentscompose.core.extensions.sharedprefs.SharedPrefsHelpers
import com.jeluchu.jchucomponentscompose.utils.broadcast.ShareBroadcastReceiver
import com.jeluchu.jchucomponentscompose.utils.zxing.EncodeHintType
import com.jeluchu.jchucomponentscompose.utils.zxing.qrcode.QRCodeWriter
import com.jeluchu.jchucomponentscompose.utils.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

/** ---- PERMISSIONS --------------------------------------------------------------------------- **/

fun Context.checkSelfPermissionCompat(permission: String) =
    ActivityCompat.checkSelfPermission(this, permission)

inline val Context.checkPermisionStorage: Boolean
    get() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_DENIED

/** ---- NETWORKS ------------------------------------------------------------------------------ **/

inline val Context.connectivityManager: ConnectivityManager
    get() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

@Suppress("DEPRECATION")
@SuppressLint("NewApi")
fun Context.checkNetworkState(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (!buildIsMAndLower) {
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    } else {
        val nw = connectivityManager.activeNetworkInfo ?: return false
        return when (nw.type) {
            (NetworkCapabilities.TRANSPORT_WIFI) -> true
            (NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

}

val Context.downstreamBandwidth: Int
    get() {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (buildIsMarshmallowAndUp) {
            val n = cm.activeNetwork
            if (n != null) {
                val nc = cm.getNetworkCapabilities(n)
                nc?.linkDownstreamBandwidthKbps ?: 0
            } else {
                0
            }
        } else {
            0
        }

    }

/** ---- SHARED PREFERENCES -------------------------------------------------------------------- **/

fun Context.initSharedPrefs() = SharedPrefsHelpers.init(this)

/** ---- TOASTS & UI --------------------------------------------------------------------------- **/

fun Context.shortToast(text: String) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

fun Context.longToast(text: String) = Toast.makeText(this, text, Toast.LENGTH_LONG).show()

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

/** ---- BROWSER TABS -------------------------------------------------------------------------- **/

fun Context.openInCustomTab(url: String, colorBar: Int) = customTabsWeb(url, colorBar)

@SuppressLint("UnspecifiedImmutableFlag")
@Suppress("DEPRECATION")
private fun Context.customTabsWeb(
    string: String,
    colorBar: Int = R.color.browser_actions_bg_grey
) {

    runCatching {

        val share: PendingIntent = if (buildIsMarshmallowAndUp)
            PendingIntent.getBroadcast(
                this, 0, Intent(
                    this,
                    ShareBroadcastReceiver::class.java
                ), PendingIntent.FLAG_IMMUTABLE
            ) else
            PendingIntent.getBroadcast(
                this, 0, Intent(
                    this,
                    ShareBroadcastReceiver::class.java
                ), PendingIntent.FLAG_UPDATE_CURRENT
            )

        val builder = CustomTabsIntent.Builder()

        builder.setToolbarColor(
            Color.parseColor(
                "#" + Integer.toHexString(
                    ContextCompat.getColor(
                        this,
                        colorBar
                    )
                )
            )
        )

        builder.setShowTitle(true)

        builder.setActionButton(
            BitmapFactory.decodeResource(
                resources,
                R.drawable.abc_ic_menu_share_mtrl_alpha
            ),
            "Compartir",
            share,
            true
        )

        val intent = builder.build()
        intent.launchUrl(this, Uri.parse(string))

    }.getOrElse {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(string))
        intent.putExtra(Browser.EXTRA_CREATE_NEW_TAB, true)
        intent.putExtra(Browser.EXTRA_APPLICATION_ID, packageName)
        startActivity(intent)
    }

}

/** ---- PRIVATE METHODS ----------------------------------------------------------------------- **/

val Context.layoutInflater
    get() = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

fun Context.getCompatDrawable(@DrawableRes drawableRes: Int): Drawable? =
    ContextCompat.getDrawable(this, drawableRes)

inline val Context.notificationManager: NotificationManager
    get() = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

/** ---- INTENTS ------------------------------------------------------------------------------- **/

fun Context.openPhoneCall(number: String) {
    startActivity(Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null)))
}

/** ---- CONECTION ----------------------------------------------------------------------------- **/

@Suppress("DEPRECATION")
fun Context.isRoamingConnection(): Boolean {
    var result = false
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (buildIsPAndUp) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = !actNw.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_ROAMING)
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = this.isRoaming
            }
        }
    }

    return result
}

@SuppressLint("NewApi")
@Suppress("DEPRECATION")
fun Context.isConnectionAvailable(): Boolean {

    var isAvailable = false
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    if (buildIsMarshmallowAndUp)
        connectivityManager?.activeNetworkInfo?.let {
            isAvailable = when (it.type) {
                ConnectivityManager.TYPE_WIFI -> true
                ConnectivityManager.TYPE_MOBILE -> true
                ConnectivityManager.TYPE_ETHERNET -> true
                else -> false
            }
        }
    else {
        connectivityManager?.let {
            val capabilities = it.getNetworkCapabilities(it.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                ) {
                    isAvailable = true
                }
            }
        }
    }

    return isAvailable

}

/** ---- DELETE CACHE -------------------------------------------------------------------------- **/

fun Context.deleteCache() {
    runCatching {
        val dir: File = this.cacheDir
        deleteDir(dir)
    }.getOrElse {
        it.printStackTrace()
    }
}

private fun deleteDir(dir: File?): Boolean {
    return if (dir != null && dir.isDirectory) {
        val children = dir.list()
        if (children != null) {
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        dir.delete()
    } else if (dir != null && dir.isFile) dir.delete()
    else false
}

/** ---- LOCALE -------------------------------------------------------------------------------- **/

@Suppress("DEPRECATION")
fun Context.getLocale(): Locale =
    if (buildIsNougatAndUp) resources.configuration.locales[0]
    else resources.configuration.locale

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

/** ---- RATE US ------------------------------------------------------------------------------- **/

fun Context.rateUs(packageName: String, @ColorRes customTabColor: Int) {
    runCatching {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(
                "https://play.google.com/store/apps/details?id=${packageName}"
            )
            setPackage("com.android.vending")
        }
        startActivity(intent)
    }.getOrElse {
        openInCustomTab(
            url = "https://play.google.com/store/apps/details?id=${packageName}",
            colorBar = customTabColor
        )
    }

}

/** ---- INTENTS ------------------------------------------------------------------------------- **/

fun Context.share(
    title: String,
    message: String
) = with(Intent()) {
    action = Intent.ACTION_SEND
    type = INTENT_TYPE_TEXT
    putExtra(Intent.EXTRA_TEXT, message)
    startActivity(Intent.createChooser(this, title))
}

fun Context.share(
    title: String,
    nameOfImage: String,
    message: String,
    bitmap: Bitmap
) {

    val bytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, nameOfImage, null)
    val uri = Uri.parse(path)

    with(Intent()) {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, message)
        putExtra(Intent.EXTRA_STREAM, uri)
        type = INTENT_TYPE_IMG_PNG
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(this, title))
    }

}

fun Context.share(
    title: String,
    message: String,
    uri: Uri
) = with(Intent()) {
    action = Intent.ACTION_SEND
    putExtra(Intent.EXTRA_TEXT, message)
    putExtra(Intent.EXTRA_STREAM, uri)
    type = INTENT_TYPE_IMG_PNG
    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    startActivity(Intent.createChooser(this, title))
}

fun Context.openOtherApp(packageName: String, packageManager: PackageManager) {
    val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
    if (launchIntent != null) startActivity(launchIntent)
}

/** ---- SAVE IMAGES --------------------------------------------------------------------------- **/

fun Context.saveBitmap(bitmap: Bitmap, filename: String = System.currentTimeMillis().toString()): Uri? {

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