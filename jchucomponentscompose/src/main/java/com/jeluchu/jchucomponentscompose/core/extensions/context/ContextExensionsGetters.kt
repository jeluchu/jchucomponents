package com.jeluchu.jchucomponentscompose.core.extensions.context

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Context.NOTIFICATION_SERVICE
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.jeluchu.jchucomponentscompose.core.extensions.packageutils.buildIsMAndLower
import com.jeluchu.jchucomponentscompose.core.extensions.sharedprefs.SharedPrefsHelpers

/** ---- NETWORKS ------------------------------------------------------------------------------ **/

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

/** ---- PRIVATE METHODS ----------------------------------------------------------------------- **/

val Context.layoutInflater
    get() = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

fun Context.getCompatDrawable(@DrawableRes drawableRes: Int): Drawable? =
    ContextCompat.getDrawable(this, drawableRes)

inline val Context.notificationManager: NotificationManager
    get() = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
