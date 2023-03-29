/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.context

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.ColorRes
import com.jeluchu.jchucomponents.ktx.constants.INTENT_TYPE_IMG_PNG
import com.jeluchu.jchucomponents.ktx.constants.INTENT_TYPE_TEXT
import com.jeluchu.jchucomponents.ktx.strings.empty
import java.io.ByteArrayOutputStream

private fun intentView(url: String) = Intent(Intent.ACTION_VIEW, Uri.parse(url))

/**
 *
 * [Context] Extension to open the Phone application
 * by passing the number you want to call as a parameter.
 * This function does not make the call directly,
 * it will only show the desired phone number in your
 * Phone application by default
 *
 * @param number [String] phone number to be moved to the Phone application
 *
 */
fun Context.openPhoneCall(number: String) = startActivity(
    Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null))
)

/**
 *
 * [Context] Extension to share a message via social networks
 * or any instant messaging application
 *
 * @param title [String] title to be displayed in the selector (by default is empty)
 * @param message [String] message to be sent
 *
 */
fun Context.share(
    title: String = String.empty(),
    message: String
) = with(Intent()) {
    action = Intent.ACTION_SEND
    type = INTENT_TYPE_TEXT
    putExtra(Intent.EXTRA_TEXT, message)
    startActivity(Intent.createChooser(this, title))
}

/**
 *
 * [Context] Extension to share a message with image
 * via social networks or any instant messaging application
 *
 * @param title [String] title to be displayed in the selector (by default is empty)
 * @param imageName [String] name of image to be sent
 * @param message [String] message to be sent
 * @param bitmap [Bitmap] image to be sent
 *
 */
@Suppress("DEPRECATION")
fun Context.share(
    title: String = String.empty(),
    imageName: String,
    message: String,
    bitmap: Bitmap
) {

    bitmap.compress(Bitmap.CompressFormat.PNG, 100, ByteArrayOutputStream())
    val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, imageName, null)
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

/**
 *
 * [Context] Extension to share a message with image
 * via social networks or any instant messaging application
 *
 * @param title [String] title to be displayed in the selector (by default is empty)
 * @param message [String] message to be sent
 * @param uri [Uri] uri to be sent
 *
 */
fun Context.share(
    title: String = String.empty(),
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

/**
 *
 * [Context] Extension to open any application
 * that is installed inside the device
 *
 * @param packageName [String] packageName of app to be open
 * @param packageManager [PackageManager] from your project
 *
 * @see android.content.pm.PackageManager
 *
 */
fun Context.openOtherApp(packageName: String, packageManager: PackageManager) {
    val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
    if (launchIntent != null) startActivity(launchIntent)
}

/**
 *
 * [Context] Extension to open Google Maps application
 * to navigate with coordinates (latitude and longitude)
 *
 * @param latitude [Double] coordinate
 * @param longitude [Double] coordinate
 *
 */
fun Context.openNavigationMaps(
    latitude: Double,
    longitude: Double,
) = intentView("http://maps.google.com/maps?daddr=$latitude,$longitude").apply {
    setPackage("com.google.android.apps.maps")
    startActivity(this)
}

/**
 *
 * [Context] Extension to open the Instagram profile, if you have the application installed
 * it will open the profile in the application, in case you do not have it installed
 * it will open a Chrome Tab with the function [openInCustomTab]
 *
 * @param username [String] the profile to be displayed
 *
 */
fun Context.openInstagram(username: String) =
    runCatching {
        startActivity(intentView("http://instagram.com/_u/$username").setPackage("com.instagram.android"))
    }.getOrElse {
        openInCustomTab("http://instagram.com/$username")
    }

/**
 *
 * [Context] Extension to open the Twitter profile, if you have the application installed
 * it will open the profile in the application, in case you do not have it installed
 * it will open a Chrome Tab with the function [openInCustomTab]
 *
 * @param username [String] the profile to be displayed
 *
 */
fun Context.openTwitter(username: String) =
    runCatching {
        startActivity(intentView("twitter://user?screen_name=$username"))
    }.getOrElse {
        openInCustomTab("https://twitter.com/#!/$username")
    }

/**
 *
 * [Context] Extension to open the YouTube video or channel, if you have the application installed
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
fun Context.openYoutube(videoId: String = String.empty(), channelId: String = String.empty()) {

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

/**
 *
 * [Context] Extension to open the Twitch profile, if you have the application installed
 * it will open the profile in the application, in case you do not have it installed
 * it will open a Chrome Tab with the function [openInCustomTab]
 *
 * @param username [String] the profile to be displayed
 *
 */
fun Context.openTwitchProfile(username: String) =
    runCatching {
        startActivity(intentView("https://www.twitch.com/_u/$username"))
    }.getOrElse {
        openInCustomTab("https://www.twitch.com/$username")
    }

/**
 *
 * [Context] Extension to open the Twitch profile, if you have the application installed
 * it will open the profile in the application, in case you do not have it installed
 * it will open a Chrome Tab with the function [openInCustomTab]
 *
 * @param username [String] the profile to be displayed
 *
 */
fun Context.rateUs(packageName: String, @ColorRes customTabColor: Int) =
    runCatching {
        startActivity(
            intentView("https://play.google.com/store/apps/details?id=${packageName}").setPackage(
                "com.android.vending"
            )
        )
    }.getOrElse {
        openInCustomTab(
            url = "https://play.google.com/store/apps/details?id=${packageName}",
            colorBar = customTabColor
        )
    }