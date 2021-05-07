package com.pieta.weatherapp.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock

class AlarmCreator {
    companion object
    {
        fun createAlarm(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(context, AlarmReceiver::class.java)

            val existingIntent =
                    PendingIntent.getService(context, 0, alarmIntent,
                                             PendingIntent.FLAG_NO_CREATE)
            if(existingIntent != null)
            {
                val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)

                alarmManager.setInexactRepeating(
                        AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_HOUR,
                        AlarmManager.INTERVAL_HALF_HOUR,
                        pendingIntent
                )

            }
        }
    }
}