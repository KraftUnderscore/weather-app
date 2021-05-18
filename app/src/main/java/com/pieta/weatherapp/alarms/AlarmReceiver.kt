package com.pieta.weatherapp.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import com.pieta.weatherapp.MainActivity
import com.pieta.weatherapp.R
import com.pieta.weatherapp.data.*
import kotlin.concurrent.thread

class AlarmReceiver : BroadcastReceiver() {
    private val tenSecInMillis = 10 * 1000L

    override fun onReceive(context: Context?, intent: Intent?) {
        if(context == null) return
        if(MainActivity.isRunning) return

        val wakeLock: PowerManager.WakeLock = acquireWakeLock(context)
        val requestHandler = RequestHandler(context)
        val serializer = Serializer()
        val handler = handleResponse(serializer, context, wakeLock)

        val passLocation = { lat: Float, lon: Float ->
            requestHandler.lat = lat
            requestHandler.lon = lon
            thread {
                val city = requestHandler.getCity()
                serializer.saveLastCityName(city, context)
            }
            requestHandler.fetch(handler)
        }

        LocationHelper.getLocation(context, passLocation)
    }

    private fun handleResponse(serializer: Serializer, context: Context, wakeLock: PowerManager.WakeLock): (String) -> Unit {
        return { s: String ->
            val responseParser = ResponseParser()
            val (d, h) = responseParser.parse(s)
            val now = h?.get(0)
            if (now != null) {
                val settings = serializer.loadNotifications(context)?.let { serializer.deserializeNotifications(it) }
                if (settings != null) {
                    val notificationMessage = ContentManager.buildNotificationMessage(context, settings, now)
                    NotificationsManager.sendNotification(context, notificationMessage)
                }
            }
            serializer.updateWeatherDataHelper(d, h, context)
            wakeLock.release()
        }
    }

    private fun acquireWakeLock(context: Context): PowerManager.WakeLock {
        val wakeLock: PowerManager.WakeLock =
                (context.getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                    newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, context.getString(R.string.wake_lock_id)).apply {
                        acquire(tenSecInMillis)}}
        return wakeLock
    }

}