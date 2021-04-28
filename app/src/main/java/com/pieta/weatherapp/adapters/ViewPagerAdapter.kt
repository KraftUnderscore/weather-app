package com.pieta.weatherapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pieta.weatherapp.R

class ViewPagerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    //TODO: do I need two? Might be one and use different view type ID
    inner class DailyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    inner class WeeklyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemViewType(position: Int): Int {
        return position % 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 0)
        {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.daily_view, parent, false)
            return DailyViewHolder(view)
        }
        else
        {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.week_view, parent, false)
            return WeeklyViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType)
        {
            0 -> {
                val viewHolder =  holder as DailyViewHolder
                //TODO: set daily values here
            }
            1 -> {
                val viewHolder =  holder as WeeklyViewHolder
                //TODO: set weekly values here
            }
        }
    }
}