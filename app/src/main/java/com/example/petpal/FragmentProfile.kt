package com.example.petpal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.petpal.databinding.FragmentProfileBinding
import com.example.petpal.shared_view_models.MainSharedViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FragmentProfile : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private val sharedViewModel : MainSharedViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var usr = Firebase.auth.currentUser
        if(usr == null){

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("DataIshere","${sharedViewModel.userData}")
        binding.textViewDogName.text= sharedViewModel.userData.get("Name").toString()
        binding.textView2DogDesc.text = sharedViewModel.userData.get("Description").toString()

        eventClicks()

    }
    private fun eventClicks(){
        binding.imageButtonStatus.setOnClickListener{
            //logic for status
        }
        binding.imageButtonProfile.setOnClickListener{
            //logic for profile
            findNavController().navigate(R.id.actoin_goto_settings)
        }
        binding.imageButtonFriends.setOnClickListener{
            //logic for friends
        }
        binding.imageButtonDelete.setOnClickListener{
            //logic for delete
        }
        binding.imageLogout.setOnClickListener{
            Firebase.auth.signOut()
            requireActivity().finish()
            //logic for loggout
        }
    }
}