/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jchucomponents.utils.firebase

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import java.io.Serializable

/**
 *
 * Author: @Jeluchu
 *
 * This class is used to send custom analytics to Firebase
 *
 * Send an event
 * [logEvent] @param event name
 * [logEvent] @param properties for event
 *
 * Send user property
 * [setUserProperty] @param key of property
 * [setUserProperty] @param value of property
 *
 * Set `user id` for firebase analytic
 * [setUserId] @param userId [please follow the firebase document rule]
 *
 */
class FirebaseAnalyticsManager(private val context: Context) {

    private val mFirebaseAnalytics: FirebaseAnalytics
        get() {
            return FirebaseAnalytics.getInstance(context)
        }

    fun logEvent(event: String, properties: Map<String, Any>) {
        val bundle = Bundle().apply {
            properties.forEach {
                when (val value: Any = it.value) {
                    is Int -> putInt(it.key, value)
                    is Long -> putLong(it.key, value)
                    is String -> putString(it.key, value)
                    is Parcelable -> putParcelable(it.key, value)
                    is Serializable -> putSerializable(it.key, value)
                    else -> Log.e("error::", "unknown key: $it.key and value: $value")
                }
            }
        }

        mFirebaseAnalytics.logEvent(event, bundle)
    }

    fun setUserProperty(key: String, value: String) =
        mFirebaseAnalytics.setUserProperty(key, value)

    fun setUserId(userId: String) =
        mFirebaseAnalytics.setUserId(userId)

}