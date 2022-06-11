package com.example.petpal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.petpal.databinding.FragmentProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FragmentProfile : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

            //logic for loggout
        }
    }
}