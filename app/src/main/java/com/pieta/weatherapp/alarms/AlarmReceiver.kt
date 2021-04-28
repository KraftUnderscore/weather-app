package com.pieta.weatherapp.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import com.pieta.weatherapp.data.RequestHandler
import com.pieta.weatherapp.data.ResponseParser
import com.pieta.weatherapp.data.Serializer

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(context == null) return
        val wakeLock: PowerManager.WakeLock =
                (context.getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                    newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag").apply {
                        acquire(1*60*1000L /*1 minute*/)
                    }
                }
        val requestHandler = context.let { RequestHandler(it) }

        val handler = { s: String ->
            val responseParser = ResponseParser()
            val serializer = Serializer()
            responseParser.parse(s)
            // TODO: Send notification HERE
            val serialized = serializer.serializeWeather(responseParser.daily, responseParser.hourly);

            serializer.saveWeather(serialized, context)
            wakeLock.release()
        }
        requestHandler.run(handler)
    }

}