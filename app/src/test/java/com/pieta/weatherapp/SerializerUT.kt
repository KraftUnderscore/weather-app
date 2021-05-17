package com.pieta.weatherapp

import com.pieta.weatherapp.data.*
import org.junit.Test

import org.junit.Assert.*

class SerializerUT {
    private val serializer = Serializer()
    private val serializedString = "[{\"dt\" : 10, \"pop\" : 0.5, \"temp\" : {\"day\" : 15.3}, \"weather\" : [{\"description\" : \"desc\", \"icon\" : \"abc\", \"id\" : 20, \"main\" : \"test\"}]}];-;[{\"dt\" : 30, \"feels_like\" : -12.05, \"humidity\" : 45, \"pop\" : 0.05, \"temp\" : -12.34, \"weather\" : [{\"description\" : \"desc2\", \"icon\" : \"abc2\", \"id\" : 40, \"main\" : \"test2\"}], \"wind_deg\" : 285, \"wind_speed\" : 26.1}]"

    private val daily = Daily(10, Temp(15.3f),
            listOf(Weather(20, "test", "desc", "abc")), 0.5f)
    private val hourly = Hourly(30, -12.34f, -12.05f, 45, 26.1f,
            285, listOf(Weather(40, "test2", "desc2", "abc2")), 0.05f)

    @Test
    fun serialize_data() {
        val result = serializer.serializeWeather(listOf(daily), listOf(hourly))
        assertEquals(serializedString, result)
    }

    @Test
    fun serialize_null() {
        val result = serializer.serializeWeather(null, null)
        assertEquals("", result)
    }

    @Test
    fun deserialize_data() {
        val (d, h) = serializer.deserializeWeather(serializedString)
        assertEquals(daily.dt, d?.get(0)?.dt)
        assertEquals(daily.weather[0].description, d?.get(0)?.weather?.get(0)?.description)
        assertEquals(daily.pop, d?.get(0)?.pop)

        assertEquals(hourly.dt, h?.get(0)?.dt)
        assertEquals(hourly.weather[0].description, h?.get(0)?.weather?.get(0)?.description)
        assertEquals(hourly.pop, h?.get(0)?.pop)
    }

    @Test
    fun deserialize_data_corrupted() {
        val corrupted = "[{\"dt\" : 10 : 15.3}, \"weather\" : [{\"descript : \"abc\", \"id\" : 20, \"main\" : \"test\"}]}];-;[{\"dt\" : 30, \" : 45, \"pop\" : 0.05, \"temp\" : -12.34,description\" : \"desc2\", \"icon\" : \"abc2\", \"id\" : 40, \"main\" : \"test2\"}], \"wind_deg\" : 285, \"wind_speed\" : 26.1}]"
        val (d, h) = serializer.deserializeWeather(corrupted)
        assertNull(d)
        assertNull(h)
    }

    @Test
    fun deserialize_data_single_string() {
        val singleString = "[{\"dt\" : 10, \"pop\" : 0.5, \"temp\" : {\"day\" : 15.3}, \"weather\" : [{\"description\" : \"desc\", \"icon\" : \"abc\", \"id\" : 20, \"main\" : \"test\"}]}]"
        val (d, h) = serializer.deserializeWeather(singleString)
        assertNull(d)
        assertNull(h)
    }
}