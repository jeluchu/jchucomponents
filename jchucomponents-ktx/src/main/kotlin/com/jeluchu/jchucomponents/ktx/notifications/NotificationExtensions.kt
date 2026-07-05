/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.jchucomponents.ktx.notifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

/**
 * Create notification group
 */
fun Context.createNotificationChannelGroup(
    @StringRes groupId: Int,
    @StringRes groupName: Int
) {
    val id = this.getString(groupId)
    val name = this.getString(groupName)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationManager = this.getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannelGroup(NotificationChannelGroup(id, name))
    }
}

/**
 * Get notification channel
 * @param channelId
 * @return [NotificationChannel]
 */
fun Context.getNotificationChannel(
    @StringRes channelId: Int
): NotificationChannel? = NotificationManagerCompat.from(this).getNotificationChannel(this.getString(channelId))

/**
 * Get notification channel group
 * @param groupId
 * @return [NotificationChannelGroup]
 */
fun Context.getNotificationChannelGroup(
    @StringRes groupId: Int
): NotificationChannelGroup? = NotificationManagerCompat.from(this).getNotificationChannelGroup(this.getString(groupId))

/**
 * Create notification channel
 */
fun Context.createNotificationChannel(
    @StringRes channelId: Int,
    @StringRes channelName: Int,
    @StringRes channelDescription: Int,
    @SuppressLint("InlinedApi") importance: Int = NotificationManager.IMPORTANCE_DEFAULT
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val id = this.getString(channelId)
        val name = this.getString(channelName)
        val descriptionText = this.getString(channelDescription)
        val channel =
            NotificationChannel(id, name, importance).also {
                it.description = descriptionText
                if (importance >= NotificationManager.IMPORTANCE_HIGH) {
                    it.enableVibration(true)
                    it.enableLights(true)
                } else if (importance < NotificationManager.IMPORTANCE_DEFAULT) {
                    it.enableVibration(false)
                    it.enableLights(false)
                    it.setSound(null, null)
                }
            }
        val notificationManager = this.getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(channel)
    }
}

/**
 * Build a notification
 */
fun Context.getNotificationBuilder(
    @StringRes channelId: Int,
    title: Int,
    text: Int,
    @DrawableRes icon: Int,
    @StringRes groupId: Int,
    importance: Int = NotificationCompat.PRIORITY_DEFAULT,
    pendingIntent: PendingIntent? = null
): NotificationCompat.Builder =
    NotificationCompat
        .Builder(this, this.getString(channelId))
        .setSmallIcon(icon)
        .setContentTitle(this.getString(title))
        .setContentText(this.getString(text))
        .setPriority(importance)
        .setContentIntent(pendingIntent)
        .setGroup(this.getString(groupId))

/**
 * Update notification
 */
fun Context.notifyNotification(
    notificationId: Int,
    notification: Notification
) {
    val canPostNotifications =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

    if (canPostNotifications) {
        NotificationManagerCompat.from(this).notify(notificationId, notification)
    }
}

/**
 * Cancel notification base on notification id
 *
 * @param notificationId id to cancel
 *
 * Note:
 * - if [notificationId] is empty or default, this method
 *      will cancel all notifications
 */
fun Context.cancelNotification(vararg notificationId: Int = intArrayOf()) {
    if (notificationId.isEmpty()) {
        NotificationManagerCompat.from(this).cancelAll()
        return
    }
    notificationId.forEach {
        NotificationManagerCompat.from(this).cancel(it)
    }
}
