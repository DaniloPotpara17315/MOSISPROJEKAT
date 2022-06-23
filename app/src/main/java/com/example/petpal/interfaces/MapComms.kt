package com.example.petpal.interfaces

import com.example.petpal.models.Event

interface MapComms {
    fun drawEventMarkers(dataset: MutableList<Event>)
    fun drawUserMarkers()
    fun filterByName(filterString : String)
}