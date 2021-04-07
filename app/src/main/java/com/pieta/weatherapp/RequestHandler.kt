package com.pieta.weatherapp

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class RequestHandler constructor(context: Context)
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
            Response.ErrorListener { f("That didn't work!") })
        queue.add(stringRequest)
    }
}