package com.pieta.weatherapp

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.gms.location.LocationServices
import com.pieta.weatherapp.adapters.ViewPagerAdapter
import com.pieta.weatherapp.alarms.AlarmCreator
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

        AlarmCreator.createAlarm(this)

        NotificationsManager.createNotificationChannel(this)
        NotificationsManager.sendNotification(this)

        val (lat: Float, lon: Float) = getLocation()
        val viewPager = findViewById<ViewPager2>(R.id.view_pager)

        initializeButtons(viewPager)
        initializeNavMenu()
        initializeAdapter(viewPager, lon, lat)
    }

    private fun initializeAdapter(viewPager: ViewPager2, lon: Float, lat: Float) {
        val serializer = Serializer()
        //val loaded = serializer.loadWeather(this)
        val loaded: String? = null
        if (loaded == null) {
            val adapter = ViewPagerAdapter("-", null, null)
            viewPager.adapter = adapter
            val requestHandler = RequestHandler(this)
            requestHandler.lon = lon
            requestHandler.lat = lat

            requestHandler.run { s: String ->
                val responseParser = ResponseParser()
                thread {
                    val city = requestHandler.getCity()
                    responseParser.parse(s)

                    val loadedAdapter = ViewPagerAdapter(city, responseParser.daily, responseParser.hourly)
                    runOnUiThread {
                        viewPager.adapter = loadedAdapter
                    }
                }
            }
        } else {
            val (d, h) = serializer.deserializeWeather(loaded)
            val adapter = ViewPagerAdapter("-", d, h)
            viewPager.adapter = adapter
        }
    }

    private fun getLocation(): Pair<Float, Float> {
        //TODO: Figure out location and permissions
        val hasFineLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val hasCoarseLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        Log.i("WeatherApp", "Got Location: coarse $hasCoarseLocationPermission fine $hasFineLocationPermission")
        if (!hasCoarseLocationPermission || !hasFineLocationPermission) {
            val permissionsToRequest = mutableListOf<String>()
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), 0)
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        var lat: Float = 0f
        var lon: Float = 0f

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                Log.i("WeatherApp", "Not empty!")
                lat = location.latitude.toFloat()
                lon = location.longitude.toFloat()
            }
        }
        Log.i("WeatherApp", "Got Location: latitude $lat longitude $lon")
        //return Pair(lat, lon)
        return Pair(51.1102f, 17.0345f)
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
    }
}