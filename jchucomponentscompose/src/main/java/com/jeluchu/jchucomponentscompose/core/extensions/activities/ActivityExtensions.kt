package com.jeluchu.jchucomponentscompose.core.extensions.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat


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

/** ---- INTENTS ------------------------------------------------------------------------------- **/

fun Activity.openInstagram(profile: String) {
    val uri: Uri = Uri.parse(profile)
    val likeIng = Intent(Intent.ACTION_VIEW, uri)

    likeIng.setPackage("com.instagram.android")

    runCatching {
        startActivity(likeIng)
    }.getOrElse {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(profile)
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
