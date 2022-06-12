package com.example.petpal.shared_view_models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.petpal.models.Profile
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainSharedViewModel : ViewModel(){
    var selectedProfile = MutableLiveData<Profile>()
    var userData = mutableMapOf<String,Any>()
    init {
        var usr = Firebase.auth.currentUser
        var data = Firebase.firestore.collection("Users").document(
            usr!!.uid).get()

        data.addOnSuccessListener { document ->
            if (document != null) {
                userData = document.data!!
                Log.d("Nodocumentfound", "User data is set ${userData}")
            }
            else {

                Log.d("Nodocumentfound", "No such document")
            }
        }.addOnFailureListener { exception ->
            Log.d("FAILURE", "get failed with ", exception)
        }
    }

}