package com.pieta.weatherapp.data

import android.content.Context
import android.util.Log
import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import java.io.StringReader
import java.lang.IllegalStateException

class Serializer {
    private val separator = ";-;"
    private val weatherStoreKey = "weather_data"
    private val cityStoreKey = "city_data"
    private val lastFetchStoreKey = "last_fetch_data"
    private val notificationsStoreKey = "notifications_data"
    private val preferencesName = "weatherApp"
    private val klaxon = Klaxon()

    fun serializeWeather(d: List<Daily>?, h:List<Hourly>?) : String {
        var output = ""
        if(d != null && h != null) {
            output += klaxon.toJsonString(d)
            output += separator
            output += klaxon.toJsonString(h)
        }
        return output
    }

    fun serializeNotifications(n: NotificationSettings?) : String {
        var output = ""
        if(n != null) output += klaxon.toJsonString(n)
        return output
    }

    fun deserializeWeather(json : String) : Pair<List<Daily>?, List<Hourly>?> {
        val split = json.split(separator, limit = 2)

        if(split.size != 2) return Pair(null, null)
        try {
            val parsedDaily = klaxon.parseJsonArray(StringReader(split[0]))
            val daily = parsedDaily.let { klaxon.parseFromJsonArray<Daily>(it) }

            val parsedHourly = klaxon.parseJsonArray(StringReader(split[1]))
            val hourly = parsedHourly.let { klaxon.parseFromJsonArray<Hourly>(it) }

            return Pair(daily, hourly)
        }
        catch (e: KlaxonException) {}
        catch (e: IllegalStateException) {}

        return Pair(null, null)
    }

    fun deserializeNotifications(json : String) : NotificationSettings? {
        try {
            val obj = klaxon.parseJsonObject(StringReader(json))
            return klaxon.parseFromJsonObject<NotificationSettings>(obj)
        }
        catch (e: KlaxonException) {}
        catch (e: IllegalStateException) {}

        return null
    }

    fun saveWeather(data: String, context: Context) {
        save(data, context, weatherStoreKey)
    }

    fun saveNotifications(data: String, context: Context) {
        save(data, context, notificationsStoreKey)
    }

    fun saveLastCityName(data: String, context: Context) {
        save(data, context, cityStoreKey)
    }

    fun saveLastFetchDate(data: Long, context: Context) {
        Log.i("WeatherApp", "Saving $data")
        save(data.toString(), context, lastFetchStoreKey)
    }

    private fun save(data: String, context: Context, key: String) {
        if(data == "") return
        val sharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString(key, data)
            apply()
        }
    }

    fun loadWeather(context: Context) : String? {
        return load(context, weatherStoreKey)
    }

    fun loadNotifications(context: Context) : String? {
        return load(context, notificationsStoreKey)
    }

    fun loadLastCity(context: Context) : String? {
        return load(context, cityStoreKey)
    }

    fun loadLastFetchDate(context: Context) : Long {
        val loaded = load(context, lastFetchStoreKey)
        Log.i("WeatherApp", "Loading $loaded")
        return if(loaded == null) 0
        else
        {
            if(loaded == "") 0
            else loaded.toLong()
        }
    }

    private fun load(context: Context, key: String) : String? {
        val sharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, "")
    }
}