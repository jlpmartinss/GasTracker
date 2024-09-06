package com.example.new_gastracker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the layout for the splash screen
        setContentView(R.layout.activity_splash)

        // Delay for 2 seconds (2000 milliseconds) before moving to MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish() // Close the SplashActivity
        }, 2000) // 2000 ms delay (2 seconds)
    }
}
