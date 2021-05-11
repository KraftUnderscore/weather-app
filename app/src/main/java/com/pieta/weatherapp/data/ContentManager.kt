package com.pieta.weatherapp.data

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import com.pieta.weatherapp.R

object ContentManager {
    fun getImage(context: Context, imgId: String) : Drawable? {
        return ResourcesCompat.getDrawable(context.resources, when(imgId) {
            "01d" -> R.drawable.ic_01
            "01n" -> R.drawable.ic_01
            "02d" -> R.drawable.ic_02
            "02n" -> R.drawable.ic_02
            "03d" -> R.drawable.ic_03
            "03n" -> R.drawable.ic_03
            "04d" -> R.drawable.ic_04
            "04n" -> R.drawable.ic_04
            "09d" -> R.drawable.ic_09
            "09n" -> R.drawable.ic_09
            "10d" -> R.drawable.ic_10
            "10n" -> R.drawable.ic_10
            "11d" -> R.drawable.ic_11
            "11n" -> R.drawable.ic_11
            "13d" -> R.drawable.ic_13
            "13n" -> R.drawable.ic_13
            "50d" -> R.drawable.ic_50
            "50n" -> R.drawable.ic_50
            else -> R.drawable.ic_03
        }, null)
    }
}