package com.example.petpal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.petpal.databinding.FragmentProfileSettingsBinding


class FragmentProfileSettings : Fragment() {

    private lateinit var binding:FragmentProfileSettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileSettingsBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageBackButton.setOnClickListener{
            //back button funkcija
            findNavController().navigate(R.id.action_backHome)
        }
        binding.imageProfile.setOnClickListener{
            //edit camera
        }
        binding.imageButtonDogName.setOnClickListener{
            //change dog name funkcija
            var dialog = FragmentConfirmNotif()
//            dialog.show()
        }
        binding.imageButtonDogDesc.setOnClickListener{
            //change doggy desc funkcija
        }
        binding.imageButtonDogBreed.setOnClickListener{
            //change doggy breed funkcija
        }
        binding.imageButtonDogEmail.setOnClickListener{
            //change email funkcija
        }
        binding.imageButtonDogPassword.setOnClickListener{
            //change password funkcija
        }
    }
}