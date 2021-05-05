package com.pieta.weatherapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.pieta.weatherapp.adapters.ViewPagerAdapter
import com.pieta.weatherapp.alarms.AlarmReceiver
import com.pieta.weatherapp.alarms.NotificationsManager
import com.pieta.weatherapp.data.RequestHandler
import com.pieta.weatherapp.data.ResponseParser
import com.pieta.weatherapp.data.Serializer
import kotlin.concurrent.thread


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
        //TODO: Figure out location and permissions
//        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//        {
//            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
//            {
//                //call function again
//            }
//            fusedLocationClient.lastLocation
//        }
//        when {
//            ContextCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED -> {
//                // You can use the API that requires the permission.
//            }
//            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) -> {
//                Toast.makeText(this, "We need permission to update the weather conditions.", Toast.LENGTH_LONG).show()
//            }
//            else -> {
//                // You can directly ask for the permission.
//                // The registered ActivityResultCallback gets the result of this request.
//                requestPermissionLauncher.launch(
//                        Manifest.permission.REQUESTED_PERMISSION)
//            }
//        }


        //TODO: Refactor initialization to separate class
        NotificationsManager.createNotificationChannel(this)
        NotificationsManager.sendNotification(this)

        val serializer = Serializer()
        //val loaded = serializer.loadWeather(this)
        val viewPager = findViewById<ViewPager2>(R.id.view_pager)

        val menuOptionsButton = findViewById<ImageView>(R.id.menu_options_button)
        val menuHourlyButton = findViewById<TextView>(R.id.menu_hourly_button)
        val menuDailyButton = findViewById<TextView>(R.id.menu_daily_button)

        menuHourlyButton.textSize = 40f
        menuDailyButton.textSize = 32f

        menuDailyButton.setOnClickListener {

            viewPager.beginFakeDrag()
            viewPager.fakeDragBy(-750f)
            viewPager.endFakeDrag()
        }

        menuHourlyButton.setOnClickListener {

            viewPager.beginFakeDrag()
            viewPager.fakeDragBy(750f)
            viewPager.endFakeDrag()
        }

        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position)
                {
                    0 -> {
                        menuHourlyButton.textSize = 40f
                        menuDailyButton.textSize = 32f
                    }
                    1 -> {
                        menuHourlyButton.textSize = 32f
                        menuDailyButton.textSize = 40f
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })
        val loaded : String? = null
        if(loaded == null)
        {
            val adapter = ViewPagerAdapter(null, null)
            viewPager.adapter = adapter
            val requestHandler = RequestHandler(this)
            requestHandler.lon = 17.03f
            requestHandler.lat = 51.1f

            requestHandler.run { s: String ->
                val responseParser = ResponseParser()
                thread {
                    responseParser.parse(s)
                    runOnUiThread {
                        adapter.notifyItemChanged(0, responseParser.hourly)
                        adapter.notifyItemChanged(1, responseParser.daily)
                    }
                }
            }
        }
        else
        {
            val (d, h) = serializer.deserializeWeather(loaded)
            val adapter = ViewPagerAdapter(d, h)
            viewPager.adapter = adapter
        }
    }
}