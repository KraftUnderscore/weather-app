package com.pieta.weatherapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.pieta.weatherapp.data.NotificationSettings
import com.pieta.weatherapp.data.Serializer

@SuppressLint("UseSwitchCompatOrMaterialCode")
class NotificationsActivity : AppCompatActivity() {
        private val serializer = Serializer()

        lateinit var notificationSettings: NotificationSettings
        lateinit var rainValue: TextView
        lateinit var humValue: TextView
        lateinit var windValue: TextView

        private lateinit var rainBar: SeekBar
        private lateinit var humBar: SeekBar
        private lateinit var windBar: SeekBar

        lateinit var rainSwitch: Switch
        lateinit var humSwitch: Switch
        lateinit var windSwitch: Switch

        lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        findAllViews()
        addAllListeners()
        displayInitialSettings()
    }

    private fun addAllListeners() {
        rainBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                rainValue.text = ("$progress%")
                notificationSettings.pop = progress.toFloat() / 100f
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        humBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                humValue.text = ("$progress%")
                notificationSettings.humidity = progress
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        windBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                windValue.text = ("$progress km/h")
                notificationSettings.wind_speed = progress.toFloat()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        rainSwitch.setOnClickListener { notificationSettings.popActive = rainSwitch.isChecked }
        humSwitch.setOnClickListener { notificationSettings.humidityActive = humSwitch.isChecked }
        windSwitch.setOnClickListener { notificationSettings.windActive = windSwitch.isChecked }

        saveButton.setOnClickListener { this.onBackPressed() }
    }

    private fun findAllViews() {
        rainValue = findViewById(R.id.notificationsRainValue)
        humValue = findViewById(R.id.notificationsHumValue)
        windValue = findViewById(R.id.notificationsWindValue)

        rainBar = findViewById(R.id.notificationsRainBar)
        humBar = findViewById(R.id.notificationsHumBar)
        windBar = findViewById(R.id.notificationsWindBar)

        rainBar.max = 100
        humBar.max = 100
        windBar.max = 100

        rainSwitch = findViewById(R.id.notificationsRainSwitch)
        humSwitch = findViewById(R.id.notificationsHumSwitch)
        windSwitch = findViewById(R.id.notificationsWindSwitch)

        saveButton = findViewById(R.id.notificationsSaveButton)
    }

    private fun displayInitialSettings() {
        val loadedSettings = serializer.loadNotifications(this)
        val settings = loadedSettings?.let { serializer.deserializeNotifications(it) }
        if (settings == null) {
            notificationSettings = NotificationSettings(false, popActive = false, pop = 0f, windActive = false, wind_speed = 0f, humidityActive = false, humidity = 0)
            rainValue.text = ("0%")
            humValue.text = ("0%")
            windValue.text = ("0 km/h")

            rainBar.progress = 0
            humBar.progress = 0
            windBar.progress = 0

            rainSwitch.isChecked = false
            humSwitch.isChecked = false
            windSwitch.isChecked = false
        } else {
            notificationSettings = settings
            rainValue.text = ("${notificationSettings.pop}%")
            humValue.text = ("${notificationSettings.humidity}%")
            windValue.text = ("${notificationSettings.wind_speed} km/h")

            rainBar.progress = (notificationSettings.pop * 100).toInt()
            humBar.progress = notificationSettings.humidity
            windBar.progress = notificationSettings.wind_speed.toInt()

            rainSwitch.isChecked = notificationSettings.popActive
            humSwitch.isChecked = notificationSettings.humidityActive
            windSwitch.isChecked = notificationSettings.windActive
        }
    }

    override fun onBackPressed() {
        val serialized = serializer.serializeNotifications(notificationSettings)
        serializer.saveNotifications(serialized, this)
        Toast.makeText(this, "Ustawienia zapisane!", Toast.LENGTH_SHORT).show()
        finish();
    }
}