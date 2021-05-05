package com.pieta.weatherapp.data

import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import java.io.StringReader
import java.lang.IllegalStateException

class ResponseParser {
    var hourly: List<Hourly>? = null
    var daily: List<Daily>? = null
    private val klaxon = Klaxon()

    fun parse(json: String) {
        hourly = null
        daily = null

        try {
            val parsed = klaxon.parseJsonObject(StringReader(json))
            val hourlyArray = parsed.array<Any>("hourly")
            val dailyArray = parsed.array<Any>("daily")
            //TODO: Cut off the arrays after 24h and 7 days
            hourly = hourlyArray?.let { klaxon.parseFromJsonArray<Hourly>(it) }
            if(hourly != null) hourly = hourly!!.dropLast(hourly!!.size - 24)
            daily = dailyArray?.let { klaxon.parseFromJsonArray<Daily>(it) }
            if(daily != null) daily = daily!!.dropLast(daily!!.size - 7)
        }
        catch (e: KlaxonException) {}
        catch (e: IllegalStateException) {}
    }
}