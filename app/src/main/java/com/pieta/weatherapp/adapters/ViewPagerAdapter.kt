package com.pieta.weatherapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pieta.weatherapp.R
import com.pieta.weatherapp.data.Daily
import com.pieta.weatherapp.data.Hourly
import java.text.SimpleDateFormat
import java.util.*

class ViewPagerAdapter(val daily: List<Daily>?, private val hourly: List<Hourly>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    inner class HourlyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private val hourlyMessageText = itemView.findViewById<TextView>(R.id.hourlyMessageText)
        private val hourlyCityText = itemView.findViewById<TextView>(R.id.hourlyCityText)
        private val hourlyActualTempText = itemView.findViewById<TextView>(R.id.hourlyActualTempText)
        private val hourlyFeelsLikeText = itemView.findViewById<TextView>(R.id.hourlyFeelsLikeText)
        private val hourlyRainText = itemView.findViewById<TextView>(R.id.hourlyRainText)
        private val hourlyHumidityText = itemView.findViewById<TextView>(R.id.hourlyHumidityText)
        private val hourlyWindSpeedText = itemView.findViewById<TextView>(R.id.hourlyWindSpeedText)
        private val hourlyWindDegreeText = itemView.findViewById<TextView>(R.id.hourlyWindDegreeText)
        private val hourlyWeatherLayout = itemView.findViewById(R.id.hourlyWeatherLayout) as LinearLayout

        fun initialize(hourly: List<Hourly>?)
        {
            if(hourly == null || hourly.isEmpty())
            {
                hourlyMessageText.text = "-"
                hourlyCityText.text = "Your city"
                hourlyActualTempText.text = "-"
                hourlyFeelsLikeText.text = "-"
                hourlyRainText.text = "-"
                hourlyHumidityText.text = "-"
                hourlyWindSpeedText.text = "-"
                hourlyWindDegreeText.text = "-"
            }
            else
            {
                val now = hourly[0]
                hourlyMessageText.text = now.weather[0].description
                hourlyCityText.text = "Your city"
                hourlyActualTempText.text = ("%.0f".format(now.temp - 273.15) + "°")
                hourlyFeelsLikeText.text = ("%.0f".format(now.feels_like - 273.15)+ "°")
                hourlyRainText.text = ("%.0f".format(100f * now.pop) + "%")
                hourlyHumidityText.text = (now.humidity.toString() + "%")
                hourlyWindSpeedText.text = (now.wind_speed.toString() + "km/h")
                hourlyWindDegreeText.text = now.wind_deg.toString()

                for (hour in hourly)
                {
                    val view = LayoutInflater.from(itemView.context).inflate(R.layout.hourly_scroll_item, hourlyWeatherLayout, true)
                    val format = SimpleDateFormat("HH':00'", Locale.GERMANY) // TODO: Fix wrong hour parsing
                    val dateString = format.format(Date(hour.dt.toLong()))
                    val hourText =  view.findViewById<TextView>(R.id.hourlyScrollItemHourText)
                    hourText.text = dateString
                    hourText.id++
                    val tempText = view.findViewById<TextView>(R.id.hourlyScrollItemTempText)
                    val formattedTemp = ("%.0f".format(hour.temp - 273.15) + "°")
                    tempText.text = formattedTemp
                    Log.i("ViewPageAdapter", "${hour.dt} parsed: $dateString temp: $formattedTemp")
                    tempText.id++
                }
            }
        }
    }

    inner class DailyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val d = daily
    }

    override fun getItemViewType(position: Int): Int {
        return position % 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == 0) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.hourly_view, parent, false)
            HourlyViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.daily_view, parent, false)
            DailyViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType)
        {
            0 -> {
                val viewHolder =  holder as HourlyViewHolder
                viewHolder.initialize(hourly)
            }
            1 -> {
                val viewHolder =  holder as DailyViewHolder
                //TODO: set weekly values here
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        when(position)
        {
            0 -> {
                val viewHolder =  holder as HourlyViewHolder
                if(payloads.size > 0) viewHolder.initialize(payloads[0] as List<Hourly>)
                else viewHolder.initialize(null)
            }
            1 -> {
                val viewHolder =  holder as DailyViewHolder
                //TODO: set weekly values here
            }
        }    }
}