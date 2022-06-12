package com.example.petpal

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
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
        Glide.with(this).load(sharedViewModel.profileImg).into(binding.imageProfile)
        var stat= sharedViewModel.userData.get("Status").toString()
        if(stat == "Druzeljubiv"){
            binding.imageSwitchKnob.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_dark));
            binding.imageSwitch.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_light));
            binding.imageSwitchKnobP.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_dark));

        }
        else if(stat == "Nezainteresovan") {
            binding.imageSwitchKnob.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.yellow_dark));
            binding.imageSwitch.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.yellow));
            binding.imageSwitchKnobP.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.yellow_dark));
        }
        else if(stat == "Agresivan"){
            binding.imageSwitchKnob.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red_dark));
            binding.imageSwitch.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red));
            binding.imageSwitchKnobP.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red_dark));
        }
        else{
            binding.imageSwitchKnob.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_dark));
            binding.imageSwitch.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_light));
            binding.imageSwitchKnobP.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_dark));
        }
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
            binding.imageEditDeleteProfDec.visibility = View.VISIBLE
            binding.imageEditDeleteProfConf.visibility = View.VISIBLE
        }
        binding.imageEditDeleteProfDec.setOnClickListener {
            binding.imageEditDeleteProfDec.visibility = View.INVISIBLE
            binding.imageEditDeleteProfConf.visibility = View.INVISIBLE
        }
        binding.imageEditDeleteProfConf.setOnClickListener {
            //add code for deletion of profile and all it's data
            binding.imageEditDeleteProfDec.visibility = View.INVISIBLE
            binding.imageEditDeleteProfConf.visibility = View.INVISIBLE
        }
        binding.imageLogout.setOnClickListener{
            Firebase.auth.signOut()
            requireActivity().finish()
            //logic for loggout
        }
    }
}