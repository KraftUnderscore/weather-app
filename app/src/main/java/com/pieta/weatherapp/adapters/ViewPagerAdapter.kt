package com.pieta.weatherapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pieta.weatherapp.R
import com.pieta.weatherapp.data.ContentManager
import com.pieta.weatherapp.data.Daily
import com.pieta.weatherapp.data.Hourly
import java.text.SimpleDateFormat
import java.util.*

class ViewPagerAdapter(private val city: String, private val daily: List<Daily>?, private val hourly: List<Hourly>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
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
        private val hourlyWindDegreeImage = itemView.findViewById<ImageView>(R.id.hourlyWindDegreeImage)
        private val hourlyWeatherLayout = itemView.findViewById(R.id.hourlyWeatherLayout) as LinearLayout

        fun populateViews(hourly: List<Hourly>?)
        {
            if(hourly == null || hourly.isEmpty()) populateEmpty()
            else populateNotEmpty(hourly)
        }

        private fun populateNotEmpty(hourly: List<Hourly>) {
            val now = hourly[0]
            hourlyMessageText.text = ContentManager.getHourlyMessage(itemView.context, hourly)
            hourlyCityText.text = city
            hourlyActualTempText.text = ContentManager.formatTemp(now.temp)
            hourlyFeelsLikeText.text = ContentManager.formatTemp(now.feels_like)
            hourlyRainText.text = ContentManager.formatPop(now.pop)
            hourlyHumidityText.text = ContentManager.formatHum(now.humidity)
            hourlyWindSpeedText.text = ContentManager.formatWind(now.wind_speed)
            hourlyWindDegreeImage.rotation = (now.wind_deg - 90).toFloat()

            for (hour in hourly) populateHourItem(hour)
        }

        private fun populateHourItem(hour: Hourly) {
            val view = LayoutInflater.from(itemView.context).inflate(R.layout.hourly_scroll_item, hourlyWeatherLayout, true)
            val hourText = view.findViewById<TextView>(R.id.hourlyScrollItemHourText)
            val tempText = view.findViewById<TextView>(R.id.hourlyScrollItemTempText)
            val imageView = view.findViewById<ImageView>(R.id.hourlyScrollItemIcon)

            hourText.text = ContentManager.formatFullHour(hour.dt * 1000L)
            tempText.text = ContentManager.formatTemp(hour.temp)
            imageView.setImageDrawable(ContentManager.getIcon(itemView.context, hour.weather[0].icon))

            hourText.id = View.generateViewId()
            tempText.id = View.generateViewId()
            imageView.id = View.generateViewId()
        }

        private fun populateEmpty() {
            val emptyValue = itemView.context.getString(R.string.default_empty_value)
            hourlyMessageText.text = emptyValue
            hourlyCityText.text = emptyValue
            hourlyActualTempText.text = emptyValue
            hourlyFeelsLikeText.text = emptyValue
            hourlyRainText.text = emptyValue
            hourlyHumidityText.text = emptyValue
            hourlyWindSpeedText.text = emptyValue
            hourlyWindDegreeImage.rotation = 90f
        }
    }

    inner class DailyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private val dailyMessageText = itemView.findViewById<TextView>(R.id.dailyMessageText)
        private val dailyCityText = itemView.findViewById<TextView>(R.id.dailyCityText)
        private val dailyScrollLayout = itemView.findViewById<LinearLayout>(R.id.dailyScrollLayout)

        fun populateViews(daily: List<Daily>?)
        {
            if(daily == null) populateEmpty()
            else populateNotEmpty(daily)
        }

        private fun populateEmpty() {
            val emptyValue = itemView.context.getString(R.string.default_empty_value)
            dailyMessageText.text = emptyValue
            dailyCityText.text = emptyValue
        }

        private fun populateNotEmpty(daily: List<Daily>) {
            dailyMessageText.text = ContentManager.getDailyMessage(itemView.context, daily)
            dailyCityText.text = city
            for (day in daily) {
                populateDayItem(day)
                if(day == daily.last()) continue
                addSeparator()
            }
        }

        private fun addSeparator() {
            val horizontalLine = View(itemView.context)
            horizontalLine.setBackgroundColor(itemView.context.resources.getColor(R.color.white))
            horizontalLine.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2)
            dailyScrollLayout.addView(horizontalLine)
        }

        private fun populateDayItem(day: Daily) {
            val view = LayoutInflater.from(itemView.context).inflate(R.layout.daily_scroll_item, dailyScrollLayout, true)
            val dayText = view.findViewById<TextView>(R.id.dailyScrollDayText)
            val tempText = view.findViewById<TextView>(R.id.dailyScrollTempText)
            val popText = view.findViewById<TextView>(R.id.dailyScrollRainText)
            val imageView = view.findViewById<ImageView>(R.id.dailyScrollItemIcon)

            dayText.text = ContentManager.formatDay(day.dt * 1000L)
            tempText.text = ContentManager.formatTemp(day.temp.day)
            popText.text = ContentManager.formatPop(day.pop)
            imageView.setImageDrawable(ContentManager.getIcon(itemView.context, day.weather[0].icon))

            dayText.id = View.generateViewId()
            tempText.id = View.generateViewId()
            popText.id = View.generateViewId()
            imageView.id = View.generateViewId()
        }
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
                viewHolder.populateViews(hourly)
            }
            1 -> {
                val viewHolder =  holder as DailyViewHolder
                viewHolder.populateViews(daily)
            }
        }
    }
}