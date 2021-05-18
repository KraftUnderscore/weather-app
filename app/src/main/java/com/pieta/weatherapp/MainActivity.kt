package com.pieta.weatherapp

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
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

    companion object {
        var isRunning = false
    }

    private val serializer = Serializer()
    private lateinit var backgroundImageView: ImageView
    private lateinit var viewPager: ViewPager2
    private lateinit var mediaPlayer: MediaPlayer

    override fun onStart() {
        isRunning = true
        if(::mediaPlayer.isInitialized) mediaPlayer.start()
        super.onStart()
    }

    override fun onStop() {
        isRunning = false
        if(::mediaPlayer.isInitialized) mediaPlayer.pause()
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeUtilities()
        initializeViews()
        initializeButtons()
        initializeNavMenu()
        initializeAdapter()
    }

    private fun initializeViews() {
        viewPager = findViewById(R.id.view_pager)
        backgroundImageView = findViewById(R.id.mainBackgroundImage)
    }

    private fun initializeUtilities() {
        LocationHelper.requestPermissions(this)
        AlarmCreator.createAlarm(this)
        NotificationsManager.createNotificationChannel(this)
    }

    private fun initializeAdapter() {
        val loaded = serializer.loadWeather(this)
        if (loaded == null || loaded == "") {
            fetchNewData()
        } else {
            if(isDataOutdated()) fetchNewData()
            else loadSavedData(loaded)
        }
    }

    private fun isDataOutdated(): Boolean {
        val hourInMilliseconds = 3600000
        val time = System.currentTimeMillis()
        val loadedTime = serializer.loadLastFetchDate(this)
        return time - loadedTime + loadedTime % hourInMilliseconds > hourInMilliseconds
    }

    private fun loadSavedData(loaded: String) {
        val (d, h) = serializer.deserializeWeather(loaded)
        val cityName = serializer.loadLastCity(this) ?: "-"
        val adapter = ViewPagerAdapter(cityName, d, h)
        viewPager.adapter = adapter
        val now = h?.get(0)
        setSoundAndBackground(now)
    }

    private fun fetchNewData() {
        Toast.makeText(this, getString(R.string.toast_fetching_data), Toast.LENGTH_LONG).show()
        val adapter = ViewPagerAdapter("", null, null)
        viewPager.adapter = adapter

        val requestHandler = RequestHandler(this)
        val function = { lat: Float, lon: Float ->
            requestHandler.lon = lon
            requestHandler.lat = lat
            requestHandler.run { s: String ->
                val responseParser = ResponseParser()
                thread {
                    val city = requestHandler.getCity()
                    val (d, h) = responseParser.parse(s)
                    val loadedAdapter = ViewPagerAdapter(city, d, h)
                    serializer.saveLastCityName(city, this)
                    runOnUiThread {
                        viewPager.adapter = loadedAdapter
                        val now = h?.get(0)
                        setSoundAndBackground(now)
                    }
                    serializer.updateWeatherDataHelper(d, h, this)
                }
            }
        }

        LocationHelper.getLocation(this, function)
    }

    private fun setSoundAndBackground(now: Hourly?) {
        if (now != null) {
            backgroundImageView.setImageDrawable(ContentManager.getBackground(this, now.weather[0].icon))
            if (::mediaPlayer.isInitialized) {
                mediaPlayer.release()
            }
            mediaPlayer = MediaPlayer.create(this, ContentManager.getSound(now.weather[0].icon))
            mediaPlayer.isLooping = true
            mediaPlayer.start()
        }
    }

    private fun initializeButtons() {
        val menuHourlyButton = findViewById<TextView>(R.id.menu_hourly_button)
        val menuDailyButton = findViewById<TextView>(R.id.menu_daily_button)
        val largeSize = 40f
        val smallSize = 32f
        val dragDistance = 750f

        menuHourlyButton.textSize = largeSize
        menuDailyButton.textSize = smallSize

        menuDailyButton.setOnClickListener {
            viewPager.beginFakeDrag()
            viewPager.fakeDragBy(-dragDistance)
            viewPager.endFakeDrag()
        }

        menuHourlyButton.setOnClickListener {
            viewPager.beginFakeDrag()
            viewPager.fakeDragBy(dragDistance)
            viewPager.endFakeDrag()
        }

        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        menuHourlyButton.textSize = largeSize
                        menuDailyButton.textSize = smallSize
                    }
                    1 -> {
                        menuHourlyButton.textSize = smallSize
                        menuDailyButton.textSize = largeSize
                    }
                }
            }
        })
    }

    private fun initializeNavMenu() {
        val menuOptionsButton = findViewById<ImageView>(R.id.menu_options_button)
        val navigationDrawer = findViewById<DrawerLayout>(R.id.mainDrawerLayout)
        val navView = findViewById<NavigationView>(R.id.navView)

        menuOptionsButton.setOnClickListener {
            if (navigationDrawer.isDrawerOpen(GravityCompat.START))
                navigationDrawer.closeDrawer(GravityCompat.START)
            else navigationDrawer.openDrawer(GravityCompat.START)
        }

        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.navMenuNotifications -> openNotificationsActivity()
                R.id.navMenuCredits -> openCreditsPopup(navigationDrawer)
                R.id.navMenuUpdateLocation -> fetchNewData()
                R.id.navMenuFeedback -> openFeedbackIntent()
                R.id.navMenuRate -> openRateIntent()
            }
            true
        }
    }

    private fun openRateIntent() {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
        }
    }

    private fun openFeedbackIntent() {
        val mailIntent = Intent(Intent.ACTION_SENDTO)
        val uriText = "mailto:" + Uri.encode("246685@student.pwr.edu.pl") + "?subject=" + Uri.encode("WeatherApp Feedback")
        mailIntent.data = Uri.parse(uriText)
        startActivity(Intent.createChooser(mailIntent, getString(R.string.send_mail)))
    }

    private fun openCreditsPopup(navigationDrawer: DrawerLayout) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.credits_popout, null)
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val popupWindow = PopupWindow(popupView, width, height, true)

        popupWindow.showAtLocation(navigationDrawer, Gravity.CENTER, 0, 0)
        navigationDrawer.closeDrawer(GravityCompat.START)
        popupView.setOnTouchListener { v, event ->
            popupWindow.dismiss()
            true
        }
    }

    private fun openNotificationsActivity() {
        val intent = Intent(this, NotificationsActivity::class.java)
        startActivity(intent)
    }
}