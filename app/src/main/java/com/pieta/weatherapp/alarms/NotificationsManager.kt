package com.pieta.weatherapp.alarms

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.pieta.weatherapp.R
import java.text.SimpleDateFormat
import java.util.*

object NotificationsManager {
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "WeatherApp powiadomienia"
            val descriptionText = "Wszystkie powiadomienia dla aplikacji."
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("com.pieta.weatherapp.updateweather", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendNotification(context: Context, message: String, timestamp: Long) {
        if(message == "") return
        val notificationID = 123456

        val format = SimpleDateFormat("HH':00'", Locale.getDefault())
        val dateString = format.format(Date(timestamp))

        val channelID = "com.pieta.weatherapp.updateweather"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notification = Notification.Builder(context, channelID)
                    .setContentTitle(context.getString(R.string.notificationTitle, dateString))
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
                    .setContentTitle(context.getString(R.string.notificationTitle, dateString))
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            with(NotificationManagerCompat.from(context)) {
                notify(notificationID, notification.build())
            }
        }
    }
}
