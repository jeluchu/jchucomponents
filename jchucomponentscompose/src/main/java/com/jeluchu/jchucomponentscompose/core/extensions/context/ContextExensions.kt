package com.jeluchu.jchucomponentscompose.core.extensions.context

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.provider.Browser
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jeluchu.jchucomponentscompose.R
import com.jeluchu.jchucomponentscompose.core.extensions.coroutines.noCrash
import com.jeluchu.jchucomponentscompose.core.extensions.packageutils.buildIsMAndLower
import com.jeluchu.jchucomponentscompose.core.extensions.sharedprefs.SharedPrefsHelpers
import com.jeluchu.jchucomponentscompose.utils.broadcast.CustomTabsCopyReceiver
import com.jeluchu.jchucomponentscompose.utils.broadcast.ShareBroadcastReceiver
import java.io.IOException

/** ---- PERMISSIONS --------------------------------------------------------------------------- **/

fun Context.checkSelfPermissionCompat(permission: String) =
    ActivityCompat.checkSelfPermission(this, permission)

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

@Suppress("DEPRECATION")
private fun Context.customTabsWeb(
    string: String,
    colorBar: Int = R.color.browser_actions_bg_grey
) {
    try {

        val share = Intent(this, ShareBroadcastReceiver::class.java)
        share.action = Intent.ACTION_SEND

        val copy: PendingIntent = PendingIntent.getBroadcast(
            this, 0, Intent(
                this,
                CustomTabsCopyReceiver::class.java
            ), PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = CustomTabsIntent.Builder()
        builder.addMenuItem("Copiar Enlace", copy)

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
        //builder.setExitAnimations(this, R.anim.enter_slide_left, R.anim.exit_slide_left)
        //builder.setStartAnimations(this, R.anim.enter_slide_right, R.anim.exit_slide_right)
        builder.setActionButton(
            BitmapFactory.decodeResource(
                resources,
                R.drawable.abc_ic_menu_share_mtrl_alpha
            ),
            "Compartir",
            PendingIntent.getBroadcast(this, 0, share, 0),
            true
        )

        val intent = builder.build()
        intent.launchUrl(this, Uri.parse(string))

    } catch (e: IOException) {
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
