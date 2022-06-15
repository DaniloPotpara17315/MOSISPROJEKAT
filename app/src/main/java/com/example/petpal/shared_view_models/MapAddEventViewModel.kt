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
    var longitude = MutableLiveData<Double>()
    var latitude = MutableLiveData<Double>()

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
        Log.d(
            "DataCurrent",
            "${naziv.value},${opis.value},${datum.value},${longitude.value},${latitude.value}"
        )
        val database =
            FirebaseDatabase.getInstance("https://paw-pal-7f105-default-rtdb.europe-west1.firebasedatabase.app/")
        val dataRef =
            database.reference// << Ovde podesavas sta gadjas iz realtime-a U ovo slucaju je test

        var newEvent = mapOf(
            "eventName" to naziv.value,
            "eventDesc" to opis.value,
            "eventDate" to datum.value,
            "eventLon" to longitude.value,
            "eventLat" to latitude.value
        )

        dataRef.child("map").child("events").push().setValue(newEvent).addOnCompleteListener{
            if(it.isSuccessful)
            {
                Log.d("Task","Uspesno uradjeno")
                clearData()
            }
            else{
                Log.d("Task","Error ${it.exception}")
            }
        }

//        Toast.makeText(,"${naziv.value},${opis.value},${datum.value},${longitude.value},${latitude.value}",Toast.LENGTH_LONG).show()
        //ako mozes stavi da se dodaje novi event u bazu odavde umesto iz fragmenta.
    }
}