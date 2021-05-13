package com.pieta.weatherapp.data

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import com.pieta.weatherapp.R
import kotlin.random.Random

object ContentManager {

    private val weatherConditions : HashMap<String, WeatherCondition> = hashMapOf(
        "01" to WeatherCondition(R.drawable.ic_01, R.array.con_01), // clear sky
        "02" to WeatherCondition(R.drawable.ic_02, R.array.con_02), // few clouds
        "03" to WeatherCondition(R.drawable.ic_03, R.array.con_03), // scattered clouds
        "04" to WeatherCondition(R.drawable.ic_04, R.array.con_04), // broken clouds
        "09" to WeatherCondition(R.drawable.ic_09, R.array.con_09), // shower rain
        "10" to WeatherCondition(R.drawable.ic_10, R.array.con_10), // rain
        "11" to WeatherCondition(R.drawable.ic_11, R.array.con_11), // thunderstorm
        "13" to WeatherCondition(R.drawable.ic_13, R.array.con_13), // snow
        "50" to WeatherCondition(R.drawable.ic_50, R.array.con_50)  // mist
    )

    class WeatherCondition(private val imgId: Int, private val stringArrayId: Int) {
        fun getImage(context: Context) : Drawable? {
            return ResourcesCompat.getDrawable(context.resources, imgId, null)
        }
        fun getMessage(context: Context) : String {
            val array = context.resources.getStringArray(stringArrayId)
            return array[Random.nextInt(0, array.size)]
        }
    }

    fun getImage(context: Context, imgId: String) : Drawable? {
        return weatherConditions[imgId.substring(0, 2)]?.getImage(context)
    }

    fun getHourlyMessage(context: Context, hourly: List<Hourly>) : String {
        val conditionsMap: HashMap<String, Int> = HashMap()

        for(hour in hourly) {
            val condition = hour.weather[0].icon.substring(0, 2)
            val currentValue = conditionsMap[condition]
            if(currentValue == null) {
                conditionsMap[condition] = 1
            } else {
                conditionsMap[condition] = currentValue + 1
            }
        }

        val sortedMap = conditionsMap.toSortedMap(compareByDescending { it })
        var message = ""
        var i = 0
        for (pair in sortedMap) {
            message += weatherConditions[pair.key]?.getMessage(context) ?: ""
            i++
            if(i==2) break
            message += "\n"
            message += "\n"
        }

        return message
    }

    fun getDailyMessage(context: Context, hourly: List<Daily>) : String {
        val conditionsMap: HashMap<String, Int> = HashMap()

        for(hour in hourly) {
            val condition = hour.weather[0].icon.substring(0, 2)
            val currentValue = conditionsMap[condition]
            if(currentValue == null) {
                conditionsMap[condition] = 1
            } else {
                conditionsMap[condition] = currentValue + 1
            }
        }

        val sortedMap = conditionsMap.toSortedMap(compareByDescending { it })
        var message = ""
        var i = 0
        for (pair in sortedMap) {
            message += weatherConditions[pair.key]?.getMessage(context) ?: ""
            i++
            if(i==2) break
            message += "\n"
            message += "\n"
        }

        return message
    }

    fun buildNotification(context: Context, settings: NotificationSettings, hour: Hourly): String {
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
}