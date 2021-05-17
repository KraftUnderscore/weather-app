package com.pieta.weatherapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.navigation.NavigationView
import com.pieta.weatherapp.adapters.ViewPagerAdapter
import com.pieta.weatherapp.alarms.AlarmCreator
import com.pieta.weatherapp.alarms.NotificationsManager
import com.pieta.weatherapp.data.*
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    private val serializer = Serializer()
    private lateinit var backgroundImage: ImageView
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LocationHelper.requestPermissions(this)

        AlarmCreator.createAlarm(this)
        NotificationsManager.createNotificationChannel(this)

        viewPager = findViewById(R.id.view_pager)
        backgroundImage = findViewById(R.id.mainBackgroundImage)
        initializeButtons()
        initializeNavMenu()
        initializeAdapter()
    }

    private fun initializeAdapter() {
        val loaded = serializer.loadWeather(this)
        if (loaded == null || loaded == "") {
            updateAll()
        } else {
            val (d, h) = serializer.deserializeWeather(loaded)
            val cityName = serializer.loadLastCity(this) ?: "-"
            val adapter = ViewPagerAdapter(cityName, d, h)
            viewPager.adapter = adapter
            val now = h?.get(0)
            if (now != null) {
                backgroundImage.setImageDrawable(ContentManager.getBackground(this, now.weather[0].icon))
            }
        }
    }

    private fun updateAll() {
        Toast.makeText(this, "Pobieram dane pogodowe...", Toast.LENGTH_LONG).show()
        val adapter = ViewPagerAdapter("-", null, null)
        viewPager.adapter = adapter

        val requestHandler = RequestHandler(this)
        val function = { lat: Float, lon: Float ->
            requestHandler.lon = lon
            requestHandler.lat = lat

            requestHandler.run { s: String ->
                val responseParser = ResponseParser()
                thread {
                    val city = requestHandler.getCity()
                    serializer.saveLastCityName(city, this)
                    responseParser.parse(s)
                    val h = responseParser.hourly
                    val loadedAdapter = ViewPagerAdapter(city, responseParser.daily, h)
                    runOnUiThread {
                        viewPager.adapter = loadedAdapter
                        val now = h?.get(0)
                        if (now != null) {
                            backgroundImage.setImageDrawable(ContentManager.getBackground(this, now.weather[0].icon))
                        }
                    }
                    serializer.saveWeather(serializer.serializeWeather(responseParser.daily, responseParser.hourly), this)
                }
            }
        }

        LocationHelper.getLocation(this, function)
    }

    private fun initializeButtons() {
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
                R.id.navMenuCredits -> {
                    val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val popupView: View = inflater.inflate(R.layout.credits_popout, null)
                    val width = LinearLayout.LayoutParams.WRAP_CONTENT
                    val height = LinearLayout.LayoutParams.WRAP_CONTENT
                    val focusable = true

                    val popupWindow = PopupWindow(popupView, width, height, focusable)
                    popupWindow.showAtLocation(navigationDrawer, Gravity.CENTER, 0, 0)
                    navigationDrawer.closeDrawer(GravityCompat.START)
                    popupView.setOnTouchListener { v, event ->
                        popupWindow.dismiss()
                        true
                    }
                }
                R.id.navMenuUpdateLocation -> {
                    updateAll()
                }
            }
            true
        }

    }
}