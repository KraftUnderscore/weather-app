package com.pieta.weatherapp

import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import java.io.StringReader
import java.lang.IllegalStateException

data class Temp(val day: Float)
data class Daily(val dt: Int, val temp: Temp, val weather: List<Weather>, val pop: Float)
data class Weather(val id: Int, val main: String, val description: String, val icon: String)
data class Hourly(val dt: Int, val temp: Float, val feels_like: Float, val humidity: Int,
                  val wind_speed: Float, val wind_deg: Int, var weather: List<Weather>, val pop: Float)
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