package com.pieta.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val text = findViewById<TextView>(R.id.tmpTxt)
//        val handler = RequestHandler()
//        var lambda = { str: String? -> text.text = str}
//        handler.run(lambda)

        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.tmpTxt)
// ...



// Add the request to the RequestQueue.

    }
}