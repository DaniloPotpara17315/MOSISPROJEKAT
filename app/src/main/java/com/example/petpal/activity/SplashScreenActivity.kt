package com.example.petpal.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.petpal.R
import java.lang.Thread.sleep

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("henlobitch", "uso u oncreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        sleep(1000)

        val intent = Intent(this, ActivityMain::class.java)
        intent.putExtra("passerbyId", intent.getStringExtra("passerbyId") ?: "")
        startActivity(intent)

    }
}