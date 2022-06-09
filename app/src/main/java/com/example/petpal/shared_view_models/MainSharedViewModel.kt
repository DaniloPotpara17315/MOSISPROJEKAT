package com.example.petpal.shared_view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.petpal.models.Profile

class MainSharedViewModel : ViewModel(){

    var selectedProfile = MutableLiveData<Profile>()

}