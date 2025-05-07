package com.mobiai.app.broadcast

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mobiai.R
import com.mobiai.app.ui.activity.MainActivity
import java.util.Calendar

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("TAG", "onReceive: 111111")
        context?.let { showNotification(it) }

    }
    private fun showNotification(context: Context) {
        val appIntent = Intent(context, MainActivity::class.java)
        val appPendingIntent = PendingIntent.getActivity(
            context,
            0,
            appIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.group)
            .setAutoCancel(false)
            .setContentTitle("${context.getString(R.string.app_name)}")
            .setContentText(context.getString(R.string.it_time_to_learn))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(appPendingIntent)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.requestPermissions(
                        (context as Activity?)!!,
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        200
                    )
                }
                return
            }
            notify(1511, builder.build()) // Sử dụng ID duy nhất để cập nhật hoặc hủy thông báo
        }
    }
}

const val CHANNEL_ID = "notifyAlarm"