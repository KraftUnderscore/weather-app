package com.pieta.weatherapp.data

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.io.IOException
import java.util.*


class RequestHandler constructor(val context: Context)
{
    private val queue = Volley.newRequestQueue(context)

    var lat:Float = 0.0f
    var lon:Float = 0.0f

    fun run(f: (String) -> Unit) {
        val url = "https://api.openweathermap.org/data/2.5/onecall?lat=${lat}&lon=${lon}&exclude=${"current,minutely,alerts"}&appid=${"1d67834f6f7c5cdc149e4c38be9c2748"}"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                f(response)
            },
            Response.ErrorListener { f("ERROR") })
        queue.add(stringRequest)
    }

    fun getCity(): String {
        val geoCoder = Geocoder(context, Locale.getDefault())
        try {
            val address: List<Address> = geoCoder.getFromLocation(lat.toDouble(), lon.toDouble(), 1)
            if(address.isNotEmpty())
            {
                if(address[0].locality != null) return address[0].locality
            }
            else
                return "Your location"
        } catch (e: IOException) {
        } catch (e: NullPointerException) {
        }
        return "-"
    }
}