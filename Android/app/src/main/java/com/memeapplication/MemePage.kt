package com.memeapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MemePage : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_meme_page)

        // Get the IP address from the intent
        val ipAddress = intent.getStringExtra("ipAddress")

        // Get the UI components
        val memeTitle = findViewById<TextView>(R.id.memeTitle)
        val memeDescription = findViewById<TextView>(R.id.memeDescription)
        val memeAccount = findViewById<TextView>(R.id.memeAccount)
        val memeImageView = findViewById<ImageView>(R.id.memeImageView)
        val nextButton = findViewById<Button>(R.id.nextButton)
        val shareButton = findViewById<Button>(R.id.shareButton)

        // Check if the IP address is not null
        if (ipAddress != null) {
            val url = "http://$ipAddress:80/random/post"

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url, null,
                { response ->
                    Log.d("MemePage", "Response: $response")

                    // Parse the API response
                    val title = response.optString("title", "No Title")
                    val description = response.optString("description", "No Description")
                    val account = response.optString("account", "Anonymous")
                    val imgLocation = response.optString("imgLocation", "")

                    // Set data to UI components
                    memeTitle.text = title
                    memeDescription.text = description
                    memeAccount.text = "Account: $account"

                    // Load image using Glide or Picasso (if imgLocation is valid)
                    if (imgLocation.isNotEmpty()) {
                        Glide.with(this)
                            .load("http://$ipAddress:80$imgLocation")  // Full image URL
                            .into(memeImageView)
                        memeDescription.text = "http://$ipAddress:80$imgLocation"
                        Toast.makeText(this,"http://$ipAddress:80$imgLocation" , Toast.LENGTH_SHORT).show()
                    } else {
                        memeImageView.setImageResource(R.drawable.ic_launcher_background) // Fallback image
                    }

                },
                { error ->
                    Log.e("MemePage", "Error: ${error.message}")
                    Toast.makeText(this, "Request Failed: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )

            // Add the request to the Volley request queue
            Volley.newRequestQueue(this).add(jsonObjectRequest)
        } else {
            // Handle the case where IP address is null
            Log.e("MemePage", "IP address is missing")
            Toast.makeText(this, "IP address is missing", Toast.LENGTH_SHORT).show()
        }

        // Next button action
        nextButton.setOnClickListener {
            // Code to load next meme or perform any action
            Toast.makeText(this, "Next Meme", Toast.LENGTH_SHORT).show()
        }

        // Share button action
        shareButton.setOnClickListener {
            // Code to share the meme (you can implement an Intent to share the meme)
            Toast.makeText(this, "Share Meme", Toast.LENGTH_SHORT).show()
        }
    }
}
