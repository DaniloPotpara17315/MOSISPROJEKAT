package com.example.petpal.shared_view_models

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petpal.R
import com.example.petpal.models.Event
import com.example.petpal.models.Profile
import com.example.petpal.models.ProfileCoordinates
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainSharedViewModel : ViewModel(){
    var selectedProfile = MutableLiveData<Profile>()
    var userData = hashMapOf<String,Any>()
    var usr = Firebase.auth.currentUser
    var profileImg:String = " "
    var selectedEvent : Event? = null
    var selectedUserKey : String? = null
    var dataLoaded = MutableLiveData<Boolean>(false)

    var eventsEnabled = MutableLiveData(true)
    var usersEnabled = MutableLiveData(true)

    var events : MutableList<Event> = mutableListOf()
    var users : MutableList<ProfileCoordinates> = mutableListOf()


    init {
//        var usr = Firebase.auth.currentUser

    }

    companion object{
        const val databaseURL="https://paw-pal-7f105-default-rtdb.europe-west1.firebasedatabase.app/"
    }
}