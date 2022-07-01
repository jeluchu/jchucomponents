/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jchucomponents.core.extensions.activities

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

private val permissionsList = arrayOf(
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE
)

private fun Activity.getPermissionCheck(permission: String) = ActivityCompat.checkSelfPermission(
    this, permission
)

/**
 *
 * [Activity] Extension to check if the storage permit has been accepted or not,
 * if it has not been accepted, the storage permit request will be launched
 *
 * @see android.Manifest.permission.WRITE_EXTERNAL_STORAGE
 *
 */
val Activity.storagePermissionGranted: Unit
    get() {
        if (getPermissionCheck(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissionsList, 1)
        }
    }

/**
 *
 * [Activity] Extension to check the availability of Google Play Services,
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