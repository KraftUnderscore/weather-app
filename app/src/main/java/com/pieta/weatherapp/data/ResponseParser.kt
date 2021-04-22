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
            hourly = hourlyArray?.let { klaxon.parseFromJsonArray<Hourly>(it) }
            daily = dailyArray?.let { klaxon.parseFromJsonArray<Daily>(it) }
        }
        catch (e: KlaxonException) {}
        catch (e: IllegalStateException) {}
    }
}