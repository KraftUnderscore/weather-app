package com.pieta.weatherapp

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pieta.weatherapp.data.*

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class SerializerIT {
    private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val serializer = Serializer()

    private val daily = Daily(10, Temp(15.3f),
            listOf(Weather(20, "test", "desc", "abc")), 0.5f)
    private val hourly = Hourly(30, -12.34f, -12.05f, 45, 26.1f,
            285, listOf(Weather(40, "test2", "desc2", "abc2")), 0.05f)

    private val notifications = NotificationSettings(true, 0.12f, 34.56f, 78)

    @Test
    fun save_load_weather_data() {
        val json = serializer.serializeWeather(listOf(daily), listOf(hourly))

        serializer.saveWeather(json, appContext)
        val loaded = serializer.loadWeather(appContext)

        assertNotNull(loaded)

        if(loaded != null)
        {
            val (d, h) = serializer.deserializeWeather(loaded)
            assertEquals(daily.dt, d?.get(0)?.dt)
            assertEquals(daily.weather[0].description, d?.get(0)?.weather?.get(0)?.description)
            assertEquals(daily.pop, d?.get(0)?.pop)

            assertEquals(hourly.dt, h?.get(0)?.dt)
            assertEquals(hourly.weather[0].description, h?.get(0)?.weather?.get(0)?.description)
            assertEquals(hourly.pop, h?.get(0)?.pop)
        }
    }

    @Test
    fun save_load_notifications_data() {
        val json = serializer.serializeNotifications(notifications)

        serializer.saveNotifications(json, appContext)
        val loaded = serializer.loadNotifications(appContext)

        assertNotNull(loaded)

        if(loaded != null)
        {
            val n = serializer.deserializeNotifications(loaded)
            assertEquals(notifications.alerts, n?.alerts)
            assertEquals(notifications.pop, n?.pop)
            assertEquals(notifications.wind_speed, n?.wind_speed)
            assertEquals(notifications.humidity, n?.humidity)

        }
    }

    @Test
    fun save_empty_data_not_overwrite_saved_data() {
        val json = serializer.serializeWeather(listOf(daily), listOf(hourly))

        serializer.saveWeather(json, appContext)
        serializer.saveWeather("", appContext)

        val loaded = serializer.loadWeather(appContext)

        assertNotNull(loaded)

        if(loaded != null)
        {
            val (d, h) = serializer.deserializeWeather(loaded)
            assertEquals(daily.dt, d?.get(0)?.dt)
            assertEquals(daily.weather[0].description, d?.get(0)?.weather?.get(0)?.description)
            assertEquals(daily.pop, d?.get(0)?.pop)

            assertEquals(hourly.dt, h?.get(0)?.dt)
            assertEquals(hourly.weather[0].description, h?.get(0)?.weather?.get(0)?.description)
            assertEquals(hourly.pop, h?.get(0)?.pop)
        }
    }
}