package com.pieta.weatherapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import androidx.viewpager2.widget.ViewPager2
import com.pieta.weatherapp.adapters.ViewPagerAdapter
import com.pieta.weatherapp.alarms.AlarmReceiver
import com.pieta.weatherapp.alarms.NotificationsManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO: Refactor creating Alarm to a separate class
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, AlarmReceiver::class.java)

        val existingIntent = PendingIntent.getService(this, 0,
                                                      alarmIntent, PendingIntent.FLAG_NO_CREATE)
        if(existingIntent != null)
        {
            val pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0)

            alarmManager.setInexactRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 10000,
                    10000,
                    pendingIntent
            )

        }

        NotificationsManager.createNotificationChannel(this)
        NotificationsManager.sendNotification(this)
//        val sharedPreferences = getSharedPreferences("weatherApp", Context.MODE_PRIVATE)
//        val value = sharedPreferences?.getInt("test", 0)

        //val textView = findViewById<TextView>(R.id.tmpTxt)
        //textView.text = value.toString()

        val adapter = ViewPagerAdapter()
        val viewPager = findViewById<ViewPager2>(R.id.view_pager) as ViewPager2
        viewPager.adapter = adapter
    }
}