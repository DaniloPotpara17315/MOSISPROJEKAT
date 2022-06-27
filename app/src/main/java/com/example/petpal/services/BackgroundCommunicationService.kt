package com.example.petpal.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.*
import android.os.Process
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.petpal.R
import com.example.petpal.activity.ActivityMain
import com.example.petpal.helpers.FirebaseHelper
import com.example.petpal.models.ProfileCoordinates
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BackgroundCommunicationService : Service() {
    private val channelId = "PetPalChannel"
    private var currentNotifId = 0
    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    // Handler that receives messages from the thread
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.

            val notifiedMap = HashMap<String, Boolean>()

            while(true) {
                try {
                    val database =
                        FirebaseDatabase.getInstance("https://paw-pal-7f105-default-rtdb.europe-west1.firebasedatabase.app/")
                    val dataRef = FirebaseHelper.database.getReference("map")
                    dataRef.child("users").get().addOnSuccessListener {
                        val temp: HashMap<Any, Any> = it.value as HashMap<Any, Any>
                        val users: MutableList<ProfileCoordinates> = mutableListOf()
                        var currentUser: ProfileCoordinates? = null
                        temp.forEach { user ->

                            val userMap = user.value as HashMap<String, Any>


                            val newUser = ProfileCoordinates(
                                user.key as String,
                                userMap["lat"] as Double,
                                userMap["lon"] as Double
                            )
                            if (user.key == Firebase.auth.uid) currentUser = newUser
                            else users.add(newUser)
                        }
                        val distance = FloatArray(3)
                        for (user in users) {

                            Location.distanceBetween(
                                currentUser?.lat ?: 0.0,
                                currentUser?.lon ?: 0.0,
                                user.lat,
                                user.lon,
                                distance
                            )

                            if (distance[0] <= 100) {
                                /*if (notifiedMap[user.id]==false) {

                                    notifiedMap[user.id] = true*/
                                Log.d(
                                    "nearbyPerson",
                                    "There is somebody nearby!!!\n ${user.id}"
                                )
                                    createNotification(user.id)
                                    //Za potrebe testiranja je iskljucena pauza izmedju dve notifikacije
                                    //TODO ukljuciti ovo za finalnu verziju
                                    //Thread.sleep(15*6000)
                                //}
                            } else {
                                notifiedMap[user.id] = false
                                Log.d("nearbyPerson", "This person is not near:\n ${user.id}")
                            }
                        }

                    }

                } catch (e: InterruptedException) {
                    // Restore interrupt status.
                    Thread.currentThread().interrupt()
                }
                Thread.sleep(20000)
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
        }
    }

    override fun onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        createNotificationChannel()
        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()

            // Get the HandlerThread's Looper and use it for our Handler
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }

        // If we get killed, after returning from here, restart
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
    }

    fun createNotification(id : String) {


        val intent = Intent(this, ActivityMain::class.java)
        intent.putExtra("passerbyId", id)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        var builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_baseline_pets_24)
            .setContentTitle("Neko je u blizini!")
            .setContentText("Pozovite na šetnju!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(currentNotifId, builder.build())
            currentNotifId+=1
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}