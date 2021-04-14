package com.pieta.weatherapp

import android.content.Context
import android.preference.PreferenceManager
import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import java.io.StringReader
import java.lang.IllegalStateException

class Serializer {
    private val separator = ";-;"
    private val storeKey = "weather_data"
    private val preferencesName = "weatherApp"
    private val klaxon = Klaxon()

    fun serialize(d: List<Daily>, h:List<Hourly>) : String {
        var output = ""
        output += klaxon.toJsonString(d)
        output += separator
        output += klaxon.toJsonString(h)
        return output
    }

    fun deserialize(json : String) : Pair<List<Daily>?, List<Hourly>?> {
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

    fun save(data: String, context: Context) {
        if(data == "") return
        val sharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString(storeKey, data)
            apply()
        }
    }

    fun load(context: Context) : String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString(storeKey, "")
    }
}