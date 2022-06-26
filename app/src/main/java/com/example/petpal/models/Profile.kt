package com.example.petpal.models

// Klasa za predstavljanje svih korisnika u aplikaciji

data class Profile(
    var name : String,
    var status : Int = STATUS_OFFLINE,
){
    var userId:String =""
    var rate: String=""
    var rasa : String = ""
    var opis : String = ""
    var imageUri:String = ""
    var friends:MutableList<String> = mutableListOf<String>()
    // Listu prijatelja i sliku kasnije implementirati
    // Slika kad se bude povezivalo sa bazom

    //izdvojiti konstante u poseban fajl kasnije
    companion object {

        const val STATUS_ONLINE = 0
        const val STATUS_DND = 1
        const val STATUS_AGGRO = 2

        const val STATUS_INVIS = -1
        const val STATUS_OFFLINE = -2
    }
}
