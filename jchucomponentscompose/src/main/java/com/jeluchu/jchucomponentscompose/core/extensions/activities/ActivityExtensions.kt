/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponentscompose.core.extensions.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability


private val permissionsList =
    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

val Activity.permissions: Unit
    get() {
        val perm =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (perm != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                permissionsList,
                1
            )
        }
    }

/** ---- GOOGLE PLAY SERVICES ------------------------------------------------------------------ **/

fun Activity.isGooglePlayServicesAvailable(withDialog: Boolean = false): Boolean {
    val googleApiAvailability = GoogleApiAvailability.getInstance()
    val status = googleApiAvailability.isGooglePlayServicesAvailable(this)
    if (status != ConnectionResult.SUCCESS) {
        if (googleApiAvailability.isUserResolvableError(status) && withDialog)
            googleApiAvailability.getErrorDialog(this, status, 2404)?.show()
        return false
    }
    return true
}

/** ---- INTENTS ------------------------------------------------------------------------------- **/

fun Activity.openInstagram(user: String) {

    val appIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("http://instagram.com/_u/$user")
    ).setPackage("com.instagram.android")

    val webIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("http://instagram.com/$user")
    )

    runCatching {
        startActivity(appIntent)
    }.getOrElse {
        startActivity(webIntent)
    }


}

fun Activity.openTwitter(username: String) {
    runCatching {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("twitter://user?screen_name=$username")
            )
        )
    }.getOrElse {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://twitter.com/#!/$username")
            )
        )
    }
}

fun Activity.openYoutube(id: String) {

    val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$id"))

    runCatching {
        startActivity(appIntent)
    }.getOrElse {
        startActivity(webIntent)
    }

}

fun Context.openChannelInYouTube(channel: String) {

    val appIntent = Intent(
        Intent.ACTION_VIEW, Uri.parse(
            "vnd.youtube.com/channel/$channel"
        )
    )

    val webIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("http://www.youtube.com/channel/$channel")
    )

    runCatching {
        startActivity(appIntent)
    }.getOrElse {
        startActivity(webIntent)
    }

}


fun Context.openTwitchProfile(user: String) {

    val appIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://www.twitch.com/_u/$user")
    ).setPackage("tv.twitch.android.viewer")

    val webIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://www.twitch.com/$user")
    )

    runCatching {
        startActivity(appIntent)
    }.getOrElse {
        startActivity(webIntent)
    }

}