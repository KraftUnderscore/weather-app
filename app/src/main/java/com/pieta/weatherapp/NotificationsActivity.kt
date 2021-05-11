package com.pieta.weatherapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
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
                rainValue.text = progress.toString()
                notificationSettings.pop = progress.toFloat() / 100f
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        humBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                humValue.text = progress.toString()
                notificationSettings.humidity = progress
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        windBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                windValue.text = progress.toString()
                notificationSettings.wind_speed = progress.toFloat()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        rainSwitch.setOnClickListener { notificationSettings.popActive = rainSwitch.isChecked }
        humSwitch.setOnClickListener { notificationSettings.humidityActive = humSwitch.isChecked }
        windSwitch.setOnClickListener { notificationSettings.windActive = windSwitch.isChecked }
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
    }

    private fun displayInitialSettings() {
        val loadedSettings = serializer.loadNotifications(this)
        if (loadedSettings == null) {
            rainValue.text = 1.toString()
            humValue.text = 1.toString()
            windValue.text = 1.toString()

            rainBar.progress = 1
            humBar.progress = 1
            windBar.progress = 1

            rainSwitch.isChecked = false
            humSwitch.isChecked = false
            windSwitch.isChecked = false
        } else {
            notificationSettings = serializer.deserializeNotifications(loadedSettings)!!
            rainValue.text = notificationSettings.pop.toString()
            humValue.text = notificationSettings.humidity.toString()
            windValue.text = notificationSettings.wind_speed.toString()

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