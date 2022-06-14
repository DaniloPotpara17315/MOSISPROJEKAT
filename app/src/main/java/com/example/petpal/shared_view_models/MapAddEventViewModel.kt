package com.example.petpal.shared_view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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
        //ako mozes stavi da se dodaje novi event u bazu odavde umesto iz fragmenta.
    }
}