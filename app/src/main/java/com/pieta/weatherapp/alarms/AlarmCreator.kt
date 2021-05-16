package com.pieta.weatherapp.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log

class AlarmCreator {
    companion object
    {
        fun createAlarm(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(context, AlarmReceiver::class.java)

            val existingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_NO_CREATE)
            Log.i("WeatherApp", (existingIntent == null).toString())
            if(existingIntent == null)
            {
                Log.i("WeatherApp", existingIntent.toString())
                val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)

                alarmManager.setRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HOUR,
                    AlarmManager.INTERVAL_HOUR,
                    pendingIntent
                )
            }
        }
    }
}