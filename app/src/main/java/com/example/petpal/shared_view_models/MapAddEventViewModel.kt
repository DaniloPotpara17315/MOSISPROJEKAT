package com.example.petpal.shared_view_models

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.petpal.models.Event
import com.google.firebase.database.FirebaseDatabase

class MapAddEventViewModel : ViewModel() {

    var naziv = MutableLiveData<String>("")
    var opis = MutableLiveData<String>("")
    var datum = MutableLiveData<String>("")
    var longitude = MutableLiveData<Double?>()
    var latitude = MutableLiveData<Double?>()

    var creatingEvent : Boolean = false
    var placingCoordinates : Boolean = false


    fun clearData() {
        naziv.value = ""
        opis.value = ""
        datum.value = ""
        longitude.value = null
        latitude.value = null

        creatingEvent = false
        placingCoordinates = false
    }

    fun addEvent() {



//        Toast.makeText(,"${naziv.value},${opis.value},${datum.value},${longitude.value},${latitude.value}",Toast.LENGTH_LONG).show()

    }
}