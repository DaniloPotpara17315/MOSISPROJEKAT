package com.example.petpal.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.petpal.R
import com.example.petpal.services.BackgroundCommunicationService
import com.example.petpal.shared_view_models.MainSharedViewModel

class ActivitySecond : AppCompatActivity() {

    private val sharedViewModel : MainSharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        sharedViewModel.userData = this.intent.getSerializableExtra("userData")
                as HashMap<String, Any>
        sharedViewModel.profileImg = this.intent.getStringExtra("userImg") ?: " "

        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        val notifsEnabled = sharedPref.getBoolean("notificationsEnabled", false)

        sharedViewModel.notifsEnabled.value = notifsEnabled
        if (sharedViewModel.notifsEnabled.value!!) {
            Intent(this, BackgroundCommunicationService::class.java).also { intent ->
                stopService(intent)
                startService(intent)
            }
        }
    }


}