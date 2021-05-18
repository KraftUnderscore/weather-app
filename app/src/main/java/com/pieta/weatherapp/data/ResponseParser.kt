package com.pieta.weatherapp.data

import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import java.io.StringReader
import java.lang.IllegalStateException

class ResponseParser {
    private val maxHours = 24
    private val maxDays = 7
    private val klaxon = Klaxon()

    fun parse(json: String): Pair<List<Daily>?, List<Hourly>?> {
        var hourly: List<Hourly>? = null
        var daily: List<Daily>? = null

        try {
            val parsed = klaxon.parseJsonObject(StringReader(json))
            val hourlyArray = parsed.array<Any>("hourly")
            val dailyArray = parsed.array<Any>("daily")
            hourly = hourlyArray?.let { klaxon.parseFromJsonArray(it) }
            if(hourly != null) hourly = hourly.dropLast(hourly.size - maxHours)
            daily = dailyArray?.let { klaxon.parseFromJsonArray(it) }
            if(daily != null) daily = daily.dropLast(daily.size - maxDays)
        }
        catch (e: KlaxonException) {}
        catch (e: IllegalStateException) {}

        return Pair(daily, hourly)
    }
}