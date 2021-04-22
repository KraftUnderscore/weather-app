package com.pieta.weatherapp.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock

class RebootReceiver : BroadcastReceiver() {

    //TODO: Refactor creating Alarm to a separate class
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(context, AlarmReceiver::class.java)

            val existingIntent = PendingIntent.getService(context, 0,
                    alarmIntent, PendingIntent.FLAG_NO_CREATE)
            if(existingIntent != null)
            {
                val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)

                alarmManager.setInexactRepeating(
                        AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() + 10000,
                        10000,
                        pendingIntent
                )


            }
        }
    }
}