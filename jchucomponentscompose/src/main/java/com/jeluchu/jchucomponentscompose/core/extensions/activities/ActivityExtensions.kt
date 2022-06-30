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
import com.jeluchu.jchucomponentscompose.core.extensions.context.openInCustomTab
import com.jeluchu.jchucomponentscompose.core.extensions.strings.empty

private val permissionsList = arrayOf(
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE
)

private fun intentView(url: String) = Intent(Intent.ACTION_VIEW, Uri.parse(url))


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

/**
 *
 * [Activity] extension to check the availability of Google Play Services,
 * return a [Boolean] to know if they are available or not, also we have
 * the possibility to choose if we want to show a system dialog or not
 *
 * @param withDialog [Boolean] to show Google Play Services dialog
 *
 */
fun Activity.isGooglePlayServicesAvailable(withDialog: Boolean = false): Boolean {
    with(GoogleApiAvailability.getInstance()) {
        val status = isGooglePlayServicesAvailable(this@isGooglePlayServicesAvailable)
        if (status != ConnectionResult.SUCCESS) {
            if (isUserResolvableError(status) && withDialog)
                getErrorDialog(this@isGooglePlayServicesAvailable, status, 2404)?.show()
            return false
        }
        return true
    }
}

/**
 *
 * [Activity] to open the Instagram profile, if you have the application installed
 * it will open the profile in the application, in case you do not have it installed
 * it will open a Chrome Tab with the function [openInCustomTab]
 *
 * @param username [String] the profile to be displayed
 *
 */
fun Activity.openInstagram(username: String) {

    runCatching {
        startActivity(
            intentView("http://instagram.com/_u/$username").setPackage("com.instagram.android")
        )
    }.getOrElse {
        openInCustomTab("http://instagram.com/$username")
    }

}

/**
 *
 * [Activity] to open the Twitter profile, if you have the application installed
 * it will open the profile in the application, in case you do not have it installed
 * it will open a Chrome Tab with the function [openInCustomTab]
 *
 * @param username [String] the profile to be displayed
 *
 */
fun Activity.openTwitter(username: String) {

    runCatching {
        startActivity(intentView("twitter://user?screen_name=$username"))
    }.getOrElse {
        openInCustomTab("https://twitter.com/#!/$username")
    }

}

/**
 *
 * [Activity] to open the YouTube video or channel, if you have the application installed
 * it will open the video or channel in the application, in case you do not have it installed
 * it will open a Chrome Tab with the function [openInCustomTab]
 *
 * It is important to know that if one of the two parameters is not passed,
 * the function will not perform any action
 *
 * @param videoId [String] the video to be displayed for default is empty [String]
 * @param channelId [String] the channel to be displayed for default is empty [String]
 *
 */
fun Activity.openYoutube(videoId: String = String.empty(), channelId: String = String.empty()) {

    if (videoId.isNotEmpty()) {
        runCatching {
            startActivity(intentView("vnd.youtube:$videoId"))
        }.getOrElse {
            openInCustomTab("http://www.youtube.com/watch?v=$videoId")
        }
    } else if (channelId.isNotEmpty()) {
        runCatching {
            startActivity(intentView("vnd.youtube.com/channel/$channelId"))
        }.getOrElse {
            openInCustomTab("http://www.youtube.com/channel/$channelId")
        }
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