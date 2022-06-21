package com.example.petpal.dialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.petpal.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.petpal.databinding.FragmentDogRateDialogBinding
import com.example.petpal.shared_view_models.MainSharedViewModel

class DogRateDialog : BottomSheetDialogFragment() {

    private val sharedView: MainSharedViewModel by activityViewModels()
    private lateinit var binding:FragmentDogRateDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDogRateDialogBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textDogName.text = sharedView.selectedAttendee.name
        Glide.with(requireContext()).load(sharedView.selectedAttendee.imageUri).into(binding.imageProfile)
    }

}