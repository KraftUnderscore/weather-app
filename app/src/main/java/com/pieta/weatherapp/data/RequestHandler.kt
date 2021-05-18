package com.pieta.weatherapp.data

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.pieta.weatherapp.R
import java.io.IOException
import java.util.*

class RequestHandler constructor(val context: Context)
{
    private val queue = Volley.newRequestQueue(context)
    private val appId = "1d67834f6f7c5cdc149e4c38be9c2748"
    var lat:Float = 0.0f
    var lon:Float = 0.0f

    fun run(f: (String) -> Unit) {
        val url = context.getString(R.string.api_url, lat, lon, appId)
        val request = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                f(response)
            },
            Response.ErrorListener { f("ERROR") })
        queue.add(request)
    }

    fun getCity(): String {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val address: List<Address> = geocoder.getFromLocation(lat.toDouble(), lon.toDouble(), 1)
            if(address.isNotEmpty())
            {
                if(address[0].locality != null) return address[0].locality
            }
        } catch (e: IOException) {}
        catch (e: NullPointerException) {}
        return "-"
    }
}