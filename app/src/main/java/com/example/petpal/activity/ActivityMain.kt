package com.example.petpal.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.petpal.R

class ActivityMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
        }
        catch (ex:Exception){
            Log.d("DeadPrilikomPokretanja",ex.toString())
        }
    }
}