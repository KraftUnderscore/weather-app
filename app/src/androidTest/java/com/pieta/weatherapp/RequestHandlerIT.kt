package com.pieta.weatherapp

import android.content.Context
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
    private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val requestHandler = RequestHandler(appContext)

    @Test
    fun sendRequest() {
        val signal = CountDownLatch(1)

        var output = ""
        val f = { res:String -> output = res; signal.countDown()}

        requestHandler.lat = 33.441792f
        requestHandler.lon = -94.037689f
        requestHandler.run(f)

        signal.await()

        assertEquals("{\"lat\":33.4418,\"lon\":-94.0377,\"timezone\":\"America/Chicago\",\"timezone_offset\":", output.subSequence(0, 77))
    }

    @Test
    fun badRequest() {
        val signal = CountDownLatch(1)

        var output = ""
        val f = { res:String -> output = res; signal.countDown()}

        requestHandler.lat = 31233.441792f
        requestHandler.lon = -91234.037689f
        requestHandler.run(f)

        signal.await()

        assertEquals("ERROR", output)
    }
}