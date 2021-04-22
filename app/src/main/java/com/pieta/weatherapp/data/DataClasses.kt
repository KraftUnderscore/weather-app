package com.pieta.weatherapp.data

data class Temp(val day: Float)
data class Daily(val dt: Int, val temp: Temp, val weather: List<Weather>, val pop: Float)
data class Weather(val id: Int, val main: String, val description: String, val icon: String)
data class Hourly(val dt: Int, val temp: Float, val feels_like: Float, val humidity: Int,
                  val wind_speed: Float, val wind_deg: Int, var weather: List<Weather>, val pop: Float)
data class NotificationSettings(val alerts: Boolean, val pop: Float,
                                val wind_speed: Float, val humidity: Int)