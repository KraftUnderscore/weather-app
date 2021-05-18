package com.pieta.weatherapp.data

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import com.pieta.weatherapp.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.random.Random

object ContentManager {

    private const val maxMessages = 1

    private val weatherConditions : HashMap<String, WeatherCondition> = hashMapOf(
        "01" to WeatherCondition(R.drawable.ic_01, R.drawable.bg_01, R.array.con_01, R.raw.raw_01), // clear sky
        "02" to WeatherCondition(R.drawable.ic_02, R.drawable.bg_02, R.array.con_02, R.raw.raw_02), // few clouds
        "03" to WeatherCondition(R.drawable.ic_03, R.drawable.bg_03, R.array.con_03, R.raw.raw_03), // scattered clouds
        "04" to WeatherCondition(R.drawable.ic_04, R.drawable.bg_04, R.array.con_04, R.raw.raw_04), // broken clouds
        "09" to WeatherCondition(R.drawable.ic_09, R.drawable.bg_09, R.array.con_09, R.raw.raw_09), // shower rain
        "10" to WeatherCondition(R.drawable.ic_10, R.drawable.bg_10, R.array.con_10, R.raw.raw_10), // rain
        "11" to WeatherCondition(R.drawable.ic_11, R.drawable.bg_11, R.array.con_11, R.raw.raw_11), // thunderstorm
        "13" to WeatherCondition(R.drawable.ic_13, R.drawable.bg_13, R.array.con_13, R.raw.raw_13), // snow
        "50" to WeatherCondition(R.drawable.ic_50, R.drawable.bg_50, R.array.con_50, R.raw.raw_50)  // mist
    )

    class WeatherCondition(private val iconId: Int, private val backgroundId: Int, private val stringArrayId: Int, private val soundId: Int) {
        fun getIcon(context: Context) : Drawable? {
            return ResourcesCompat.getDrawable(context.resources, iconId, null)
        }

        fun getBackground(context: Context) : Drawable? {
            return ResourcesCompat.getDrawable(context.resources, backgroundId, null)
        }

        fun getMessage(context: Context) : String {
            val array = context.resources.getStringArray(stringArrayId)
            return array[Random.nextInt(0, array.size)]
        }

        fun getSound() : Int {
            return soundId
        }
    }

    fun getSound(imgId: String) : Int {
        return weatherConditions[imgId.substring(0, 2)]?.getSound() ?: R.raw.raw_10
    }

    fun getIcon(context: Context, imgId: String) : Drawable? {
        return weatherConditions[imgId.substring(0, 2)]?.getIcon(context)
    }

    fun getBackground(context: Context, imgId: String) : Drawable? {
        return weatherConditions[imgId.substring(0, 2)]?.getBackground(context)
    }

    fun getHourlyMessage(context: Context, hourly: List<Hourly>) : String {
        val conditionsMap: HashMap<String, Int> = HashMap()

        for(hour in hourly) {
            val condition = hour.weather[0].icon.substring(0, 2)
            addToHashMap(conditionsMap, condition)
        }

        return generateMessage(conditionsMap, context)
    }

    fun getDailyMessage(context: Context, daily: List<Daily>) : String {
        val conditionsMap: HashMap<String, Int> = HashMap()

        for(day in daily) {
            val condition = day.weather[0].icon.substring(0, 2)
            addToHashMap(conditionsMap, condition)
        }

        return generateMessage(conditionsMap, context)
    }

    private fun addToHashMap(conditionsMap: HashMap<String, Int>, condition: String) {
        val currentValue = conditionsMap[condition]
        if (currentValue == null) {
            conditionsMap[condition] = 1
        } else {
            conditionsMap[condition] = currentValue + 1
        }
    }

    private fun generateMessage(conditionsMap: HashMap<String, Int>, context: Context): String {
        val sortedMap = conditionsMap.toSortedMap(compareByDescending { it })
        var message = ""
        var i = 0
        for (pair in sortedMap) {
            message += weatherConditions[pair.key]?.getMessage(context) ?: ""
            i += 1
            if (i == maxMessages) break
            message += "\n\n"
        }

        return message
    }

    fun buildNotificationTitle(context: Context): String {
        val timestamp = System.currentTimeMillis()
        return context.getString(R.string.notificationTitle, formatFullHour(timestamp))
    }

    fun buildNotificationMessage(context: Context, settings: NotificationSettings, hour: Hourly): String {
        var output = ""
        if(settings.popActive && settings.pop <= hour.pop) {
            output += context.getString(R.string.notificationRainMessage, ("%.0f".format(100f * hour.pop) + "%"))
        }
        if(settings.humidityActive && settings.humidity <= hour.humidity) {
            output += "\n"
            output += context.getString(R.string.notificationHumMessage, (hour.humidity.toString() + "%"))
        }
        if(settings.windActive && settings.wind_speed <= hour.wind_speed) {
            output += "\n"
            output += context.getString(R.string.notificationWindMessage, (hour.wind_speed.toString() + "km/h"))
        }
        return output
    }

    fun formatTemp(temp: Float) = "%.0f".format(temp - 273.15) + "°"
    fun formatPop(pop: Float) = "%.0f".format(100f * pop) + "%"
    fun formatHum(humidity: Int) = "$humidity%"
    fun formatWind(wind: Float) = (wind.toString() + "km/h")

    fun formatFullHour(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH':00'", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun formatDay(timestamp: Long): String {
        val format = SimpleDateFormat("EEE", Locale.getDefault())
        return format.format(Date(timestamp))
    }
}