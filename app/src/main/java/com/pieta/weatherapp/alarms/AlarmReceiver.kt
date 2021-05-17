package com.pieta.weatherapp.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import com.pieta.weatherapp.data.*
import kotlin.concurrent.thread

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(context == null) return
        val wakeLock: PowerManager.WakeLock =
                (context.getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                    newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WeatherApp::UpdateWeather").apply {
                        acquire(10 * 1000L) // 10 seconds
                    }
                }
        val requestHandler = RequestHandler(context)
        val serializer = Serializer()
        val handler = { s: String ->
            val responseParser = ResponseParser()
            responseParser.parse(s)
            val hour = responseParser.hourly?.get(0)
            if(hour != null) {
                val settings = serializer.loadNotifications(context)?.let { serializer.deserializeNotifications(it) }
                if(settings != null) {
                    val notificationMessage = ContentManager.buildNotification(context, settings, hour)
                    NotificationsManager.sendNotification(context, notificationMessage)
                }
            }
            val serialized = serializer.serializeWeather(responseParser.daily, responseParser.hourly)
            serializer.saveWeather(serialized, context)
            wakeLock.release()
        }
        val function = { lat: Float, lon: Float ->
            requestHandler.lat = lat
            requestHandler.lon = lon
            thread {
                val city = requestHandler.getCity()
                serializer.saveLastCityName(city, context)
            }
            requestHandler.run(handler)
        }

        LocationHelper.getLocation(context, function)
    }

}