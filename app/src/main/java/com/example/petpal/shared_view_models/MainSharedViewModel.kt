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
    var userData = mutableMapOf<String,Any>()
    var profileImg:Any = " "
    var selectedEvent : Event? = null
    var selectedUserKey : String? = null
    var dataLoaded = MutableLiveData<Boolean>(false)


    var events : MutableList<Event> = mutableListOf()
    var users : MutableList<ProfileCoordinates> = mutableListOf()


    init {
        var usr = Firebase.auth.currentUser
        var data = Firebase.firestore.collection("Users").document(
            usr!!.uid
        ).get()
        data.addOnSuccessListener { document ->
            if (document != null) {
                userData = document.data!!
                dataLoaded.value = true
                val storageRef = Firebase.storage.reference
                storageRef.child("ProfileImages/${usr.uid}.png").downloadUrl.addOnSuccessListener {
                    profileImg = it
                    Log.d("ObjectName", "${profileImg}")
                }.addOnFailureListener {
                    profileImg = " "
                    Log.d("FailedToLocat", "NotFound")
                }
                Log.d("Nodocumentfound", "User data is set ${userData}")

            } else {

                Log.d("Nodocumentfound", "No such document")
            }
        }.addOnFailureListener { exception ->
            Log.d("FAILURE", "get failed with ", exception)
        }
    }

    companion object{
        const val databaseURL="https://paw-pal-7f105-default-rtdb.europe-west1.firebasedatabase.app/"
    }
}