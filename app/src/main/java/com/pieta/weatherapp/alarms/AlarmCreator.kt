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
            if(existingIntent == null)
            {
                val hourInMilliseconds = 3600000
                val elapsedTime = SystemClock.elapsedRealtime()
                val realTime = System.currentTimeMillis()
                val mod = realTime % hourInMilliseconds
                val toFullHour = hourInMilliseconds - mod
                val output = elapsedTime + toFullHour
                val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)
                alarmManager.setRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    output,
                    AlarmManager.INTERVAL_HOUR,
                    pendingIntent
                )
            }
        }
    }
}