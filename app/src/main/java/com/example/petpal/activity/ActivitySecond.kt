package com.example.petpal.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.petpal.R
import com.example.petpal.shared_view_models.MainSharedViewModel

class ActivitySecond : AppCompatActivity() {

    private val sharedViewModel : MainSharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        sharedViewModel.userData = this.intent.getSerializableExtra("userData")
                as HashMap<String, Any>
    }
}