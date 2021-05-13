package com.pieta.weatherapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.navigation.NavigationView
import com.pieta.weatherapp.adapters.ViewPagerAdapter
import com.pieta.weatherapp.alarms.AlarmCreator
import com.pieta.weatherapp.alarms.AlarmReceiver
import com.pieta.weatherapp.alarms.NotificationsManager
import com.pieta.weatherapp.data.LocationHelper
import com.pieta.weatherapp.data.RequestHandler
import com.pieta.weatherapp.data.ResponseParser
import com.pieta.weatherapp.data.Serializer
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    private val serializer = Serializer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LocationHelper.requestPermissions(this)

        //AlarmCreator.createAlarm(this)
        createAlarm(this)
        NotificationsManager.createNotificationChannel(this)

        val viewPager = findViewById<ViewPager2>(R.id.view_pager)

        initializeButtons(viewPager)
        initializeNavMenu()
        initializeAdapter(viewPager)
    }

    private fun initializeAdapter(viewPager: ViewPager2) {
        //val loaded = serializer.loadWeather(this)
        val loaded: String? = null
        if (loaded == null || loaded == "") {
            updateAll(viewPager)
        } else {
            Log.i("WeatherApp", "WasNotNull")
            val (d, h) = serializer.deserializeWeather(loaded)
            val cityName = serializer.loadLastCity(this) ?: "-"
            val adapter = ViewPagerAdapter(cityName, d, h)
            viewPager.adapter = adapter
        }
    }

    private fun updateAll(viewPager: ViewPager2) {
        Log.i("WeatherApp", "WasNull")
        val adapter = ViewPagerAdapter("-", null, null)
        viewPager.adapter = adapter

        val requestHandler = RequestHandler(this)
        val function = { lat: Float, lon: Float ->
            requestHandler.lon = lon
            requestHandler.lat = lat

            requestHandler.run { s: String ->
                Log.i("WeatherApp", "RequestFetched")
                val responseParser = ResponseParser()
                thread {
                    val city = requestHandler.getCity()
                    serializer.saveLastCityName(city, this)
                    responseParser.parse(s)
                    Log.i("WeatherApp", "RequestParsed")

                    val loadedAdapter = ViewPagerAdapter(city, responseParser.daily, responseParser.hourly)
                    runOnUiThread {
                        Log.i("WeatherApp", "AdapterReplaced")
                        viewPager.adapter = loadedAdapter
                    }
                    serializer.saveWeather(serializer.serializeWeather(responseParser.daily, responseParser.hourly), this)
                }
            }
        }

        LocationHelper.getLocation(this, function)
    }

    private fun initializeButtons(viewPager: ViewPager2) {
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
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
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
        })
    }

    private fun initializeNavMenu() {
        val menuOptionsButton = findViewById<ImageView>(R.id.menu_options_button)
        val navigationDrawer = findViewById<DrawerLayout>(R.id.mainDrawerLayout)
        menuOptionsButton.setOnClickListener {
            if (navigationDrawer.isDrawerOpen(GravityCompat.START)) {
                navigationDrawer.closeDrawer(GravityCompat.START)
            } else {
                navigationDrawer.openDrawer(GravityCompat.START)
            }
        }

        val navView = findViewById<NavigationView>(R.id.navView)
        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.navMenuNotifications -> {
                    val intent = Intent(this, NotificationsActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }

    }

    fun createAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java)

        val existingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_NO_CREATE)
        Log.i("WeatherApp", (existingIntent == null).toString())
        if(existingIntent == null)
        {
            Log.i("WeatherApp", existingIntent.toString())
            val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)

//                alarmManager.setInexactRepeating(
//                        AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                        SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_HOUR,
//                        AlarmManager.INTERVAL_HALF_HOUR,
//                        pendingIntent
//                )

            alarmManager.setRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 10 * 1000,
                    10 * 1000,
                    pendingIntent
            )
            val existingIntent2 = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_NO_CREATE) == null
            Log.i("WeatherApp", existingIntent2.toString())

        }
    }
}