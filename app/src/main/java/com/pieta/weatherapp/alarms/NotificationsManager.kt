package com.pieta.weatherapp.alarms

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.pieta.weatherapp.R
import com.pieta.weatherapp.data.ContentManager
import java.text.SimpleDateFormat
import java.util.*

object NotificationsManager {
    private const val notificationID = 123456

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notification_channel_name)
            val descriptionText = context.getString(R.string.notification_channel_desc)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(context.getString(R.string.notification_channel_id), name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendNotification(context: Context, message: String) {
        if(message == "") return

        val channelID = context.getString(R.string.notification_channel_id)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notification = Notification.Builder(context, channelID)
                    .setContentTitle(ContentManager.buildNotificationTitle(context))
                    .setContentText(message)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setChannelId(channelID)
                    .setStyle(Notification.BigTextStyle().bigText(message))
                    .build()

            val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(notificationID, notification)
        } else {
            val notification = NotificationCompat.Builder(context, channelID)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle(ContentManager.buildNotificationTitle(context))
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            with(NotificationManagerCompat.from(context)) {
                notify(notificationID, notification.build())
            }
        }
    }
}
