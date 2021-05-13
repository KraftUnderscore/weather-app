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
        Log.i("WeatherApp", "Hello?")
        if(context == null) return
        val wakeLock: PowerManager.WakeLock =
                (context.getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                    newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WeatherApp::UpdateWeather").apply {
                        acquire(15 * 1000L) // 15 seconds
                    }
                }
        Log.i("WeatherApp", "WakeLock set")
        val requestHandler = RequestHandler(context)
        val serializer = Serializer()
        val handler = { s: String ->
            val responseParser = ResponseParser()
            Log.i("WeatherApp", "Parsing response")
            responseParser.parse(s)
            Log.i("WeatherApp", "Parsed")

            val hour = responseParser.hourly?.get(0)
            if(hour != null) {
                Log.i("WeatherApp", "NotNullHour")
                val settings = serializer.loadNotifications(context)?.let { serializer.deserializeNotifications(it) }
                if(settings != null) {
                    Log.i("WeatherApp", "NotNullSettings")
                    val notificationMessage = ContentManager.buildNotification(context, settings, hour)
                    NotificationsManager.sendNotification(context, notificationMessage)
                }
            }

            Log.i("WeatherApp", "Serializing")
            val serialized = serializer.serializeWeather(responseParser.daily, responseParser.hourly);
            Log.i("WeatherApp", "Serialized")

            serializer.saveWeather(serialized, context)
            Log.i("WeatherApp", "Release Lock")
            wakeLock.release()
        }
        val function = { lat: Float, lon: Float ->
            Log.i("WeatherApp", "Got location")
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