package com.pieta.weatherapp

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.util.concurrent.CountDownLatch

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RequestHandlerIT {
    @Test
    fun sendRequest() {
        val signal = CountDownLatch(1)
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val requestHandler = RequestHandler(appContext)

        var output = ""
        val f = { res:String -> output = res; signal.countDown()}

        requestHandler.lat = 33.441792f
        requestHandler.lon = -94.037689f
        requestHandler.run(f)

        signal.await()

        assertEquals("com.pieta.weatherapp", output)
    }
}