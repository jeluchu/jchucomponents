package com.jeluchu.jchucomponentscompose.core.extensions.activities

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import io.github.g00fy2.quickie.config.BarcodeFormat
import io.github.g00fy2.quickie.config.ScannerConfig


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


/** ---- ACTIVITY RESULT LAUNCHER -------------------------------------------------------------- **/

fun ActivityResultLauncher<ScannerConfig>.openQRReader(@StringRes stringRes: Int) {
    launch(
        ScannerConfig.build {
            setBarcodeFormats(listOf(BarcodeFormat.FORMAT_QR_CODE))
            setOverlayStringRes(stringRes)
            setHapticSuccessFeedback(false)
            setShowTorchToggle(true)
            setUseFrontCamera(false)
        }
    )
}