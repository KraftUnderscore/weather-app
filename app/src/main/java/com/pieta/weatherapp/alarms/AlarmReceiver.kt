package com.pieta.weatherapp.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pieta.weatherapp.data.RequestHandler
import com.pieta.weatherapp.data.ResponseParser
import com.pieta.weatherapp.data.Serializer

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(context == null) return
        val requestHandler = context.let { RequestHandler(it) }

        val handler = { s: String ->
            val responseParser = ResponseParser()
            val serializer = Serializer()
            responseParser.parse(s)
            // TODO: Send notification HERE
            val serialized = serializer.serializeWeather(responseParser.daily, responseParser.hourly);

            serializer.saveWeather(serialized, context)

        }
        // TODO: It is run async, might need to manual WakeLock with PowerManager
        requestHandler.run(handler)
    }

}