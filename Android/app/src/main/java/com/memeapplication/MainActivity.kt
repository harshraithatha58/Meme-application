package com.memeapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {
    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Setup for edge-to-edge and padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize RequestQueue for Volley
        requestQueue = Volley.newRequestQueue(this)

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val ipAddressEditText = findViewById<EditText>(R.id.ipAddress)
            val ipAddress = ipAddressEditText.text.toString().trim()

            if (ipAddress.isNotEmpty()) {
                val url = "http://$ipAddress:80/ack/abc"

                // Make the GET request using Volley
                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.GET, url, null,
                    { response ->
                        // Successfully received a response
                        Log.d("MainActivity", "Response: $response")

                        // Pass the IP address to the MemePage activity
                        val intent = Intent(this, MemePage::class.java)
                        intent.putExtra("ipAddress", ipAddress)
                        startActivity(intent)
                    },
                    { error ->
                        // Error handling
                        Log.e("MainActivity", "Error: ${error.message}")
                        Toast.makeText(this, "Request Failed: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                )

                // Add the request to the RequestQueue
                requestQueue.add(jsonObjectRequest)
            } else {
                // Handle the case where IP address is empty
                Toast.makeText(this, "IP Address cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
