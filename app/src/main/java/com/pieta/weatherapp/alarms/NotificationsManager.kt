package com.pieta.weatherapp.alarms

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.provider.Settings.Global.getString
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.pieta.weatherapp.R

class NotificationsManager
{
    companion object {
        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "WeatherApp Notifications"
                val descriptionText = "All Notifications for WeatherApp."
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel("com.pieta.weatherapp", name, importance).apply {
                    description = descriptionText
                }
                // Register the channel with the system
                val notificationManager: NotificationManager =
                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }

        fun sendNotification(context: Context) {
            val notificationID = 123456

            val channelID = "com.pieta.weatherapp"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notification = Notification.Builder(context, channelID)
                        .setContentTitle("Example Notification")
                        .setContentText("This is an  example notification.")
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setChannelId(channelID)
                        .build()

                val notificationManager: NotificationManager =
                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(notificationID, notification)
            } else {
                val notification = NotificationCompat.Builder(context, channelID)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("Example Notification")
                        .setContentText("This is an  example notification.")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                with(NotificationManagerCompat.from(context)) {
                    notify(notificationID, notification.build())
                }
            }


        }
    }

}