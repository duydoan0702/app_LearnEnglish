package com.mobiai.app.broadcast

import android.Manifest
import android.app.Notification
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat


class NotificationManager {
    companion object{

        fun sendNotification(context: Context, notificationId: Int, notification: Notification) {
            with(NotificationManagerCompat.from(context)) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    return@with
                }
                notify(notificationId, notification)
            }
        }

        fun cancelNotification(context: Context, notificationId: Int) {
            with(NotificationManagerCompat.from(context)) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    return@with
                }
                cancel(notificationId)
            }
        }
    }
}