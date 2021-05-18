package com.pieta.weatherapp.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock

 object AlarmCreator
{
    fun createAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val existingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_NO_CREATE)

        if(existingIntent == null)
        {
            val startTime = calculateStartTime()
            val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)

            alarmManager.setRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP, startTime,
                    AlarmManager.INTERVAL_HOUR, pendingIntent
            )
        }
    }

    private fun calculateStartTime(): Long {
        val hourInMilliseconds = 3600000
        val toFullHour = hourInMilliseconds - System.currentTimeMillis() % hourInMilliseconds
        return SystemClock.elapsedRealtime() + toFullHour
    }
}