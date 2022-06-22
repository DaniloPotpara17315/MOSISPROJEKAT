package com.example.petpal.models

import android.graphics.Bitmap

data class ProfileCoordinates(
    var id : String,
    var lat : Double,
    var lon : Double,
    var status : String,
    var image : String
)
