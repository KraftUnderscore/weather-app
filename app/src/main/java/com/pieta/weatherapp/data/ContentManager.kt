package com.pieta.weatherapp.data

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import com.pieta.weatherapp.R
import kotlin.random.Random

object ContentManager {

    private const val maxHourlyMessages = 1
    private const val maxDailyMessages = 1

    private val weatherConditions : HashMap<String, WeatherCondition> = hashMapOf(
        "01" to WeatherCondition(R.drawable.ic_01, R.drawable.bg_01, R.array.con_01, R.raw.light_rain), // clear sky
        "02" to WeatherCondition(R.drawable.ic_02, R.drawable.bg_02, R.array.con_02, R.raw.light_rain), // few clouds
        "03" to WeatherCondition(R.drawable.ic_03, R.drawable.bg_03, R.array.con_03, R.raw.light_rain), // scattered clouds
        "04" to WeatherCondition(R.drawable.ic_04, R.drawable.bg_04, R.array.con_04, R.raw.light_rain), // broken clouds
        "09" to WeatherCondition(R.drawable.ic_09, R.drawable.bg_09, R.array.con_09, R.raw.light_rain), // shower rain
        "10" to WeatherCondition(R.drawable.ic_10, R.drawable.bg_10, R.array.con_10, R.raw.light_rain), // rain
        "11" to WeatherCondition(R.drawable.ic_11, R.drawable.bg_11, R.array.con_11, R.raw.light_rain), // thunderstorm
        "13" to WeatherCondition(R.drawable.ic_13, R.drawable.bg_13, R.array.con_13, R.raw.light_rain), // snow
        "50" to WeatherCondition(R.drawable.ic_50, R.drawable.bg_50, R.array.con_50, R.raw.light_rain)  // mist
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
        return weatherConditions[imgId.substring(0, 2)]?.getSound() ?: R.raw.light_rain
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
            if(i == maxHourlyMessages) break
            message += "\n"
            message += "\n"
        }

        return message
    }

    fun getDailyMessage(context: Context, daily: List<Daily>) : String {
        val conditionsMap: HashMap<String, Int> = HashMap()

        for(day in daily) {
            val condition = day.weather[0].icon.substring(0, 2)
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
            if(i == maxDailyMessages) break
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