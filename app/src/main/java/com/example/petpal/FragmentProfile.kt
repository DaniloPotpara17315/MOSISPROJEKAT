package com.example.petpal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.petpal.databinding.FragmentProfileBinding

class FragmentProfile : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        }
        binding.imageButtonFriends.setOnClickListener{
            //logic for friends
        }
        binding.imageButtonDelete.setOnClickListener{
            //logic for delete
        }
        binding.imageLogout.setOnClickListener{
            //logic for loggout
        }
    }
}