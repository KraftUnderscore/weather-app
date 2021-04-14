package com.pieta.weatherapp

import android.content.Context
import android.preference.PreferenceManager
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.util.concurrent.CountDownLatch

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class SerializerIT {
    private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val serializer = Serializer()

    private val daily = Daily(10, Temp(15.3f),
            listOf(Weather(20, "test", "desc", "abc")), 0.5f)
    private val hourly = Hourly(30, -12.34f, -12.05f, 45, 26.1f,
            285, listOf(Weather(40, "test2", "desc2", "abc2")), 0.05f)

    @Test
    fun save_load_data() {
        val json = serializer.serialize(listOf(daily), listOf(hourly))

        serializer.save(json, appContext)
        val loaded = serializer.load(appContext)

        assertNotNull(loaded)

        if(loaded != null)
        {
            val (d, h) = serializer.deserialize(loaded)
            assertEquals(daily.dt, d?.get(0)?.dt)
            assertEquals(daily.weather[0].description, d?.get(0)?.weather?.get(0)?.description)
            assertEquals(daily.pop, d?.get(0)?.pop)

            assertEquals(hourly.dt, h?.get(0)?.dt)
            assertEquals(hourly.weather[0].description, h?.get(0)?.weather?.get(0)?.description)
            assertEquals(hourly.pop, h?.get(0)?.pop)
        }
    }

    @Test
    fun save_empty_data_not_overwrite_saved_data() {
        val json = serializer.serialize(listOf(daily), listOf(hourly))

        serializer.save(json, appContext)
        serializer.save("", appContext)

        val loaded = serializer.load(appContext)

        assertNotNull(loaded)

        if(loaded != null)
        {
            val (d, h) = serializer.deserialize(loaded)
            assertEquals(daily.dt, d?.get(0)?.dt)
            assertEquals(daily.weather[0].description, d?.get(0)?.weather?.get(0)?.description)
            assertEquals(daily.pop, d?.get(0)?.pop)

            assertEquals(hourly.dt, h?.get(0)?.dt)
            assertEquals(hourly.weather[0].description, h?.get(0)?.weather?.get(0)?.description)
            assertEquals(hourly.pop, h?.get(0)?.pop)
        }
    }
}