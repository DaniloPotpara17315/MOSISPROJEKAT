package com.example.petpal.dialog

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.petpal.databinding.MapFilterDialogBinding
import com.example.petpal.shared_view_models.MainSharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MapFilterDialog : BottomSheetDialogFragment() {

    private val sharedViewModel:MainSharedViewModel by activityViewModels()
    private lateinit var binding:MapFilterDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = MapFilterDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edittextFilterRadius.setText(sharedViewModel.cutoffDistance.toString())

        val checkboxEvents = binding.checkBoxEvents
        checkboxEvents.setOnClickListener {
             sharedViewModel.eventsEnabled.value = checkboxEvents.isChecked
        }
        val checkBoxUsers = binding.checkBoxOther
        checkBoxUsers.setOnClickListener {
            sharedViewModel.usersEnabled.value = checkBoxUsers.isChecked
        }

        sharedViewModel.eventsEnabled.observe(viewLifecycleOwner) {
            checkboxEvents.isChecked = it
            setFragmentResult("eventMarkers",
                bundleOf("eventMarkers" to it))
        }
        sharedViewModel.usersEnabled.observe(viewLifecycleOwner) {
            checkBoxUsers.isChecked = it

            setFragmentResult("userMarkers",
                bundleOf("userMarkers" to it))
        }

        binding.edittextFilterRadius.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding.edittextFilterRadius.text.toString()=="") {
                    sharedViewModel.cutoffDistance = 0
                } else {
                    sharedViewModel.cutoffDistance =
                        binding.edittextFilterRadius.text.toString().toInt()
                }
                setFragmentResult("eventMarkers",
                    bundleOf("eventMarkers" to false))
            }

        })

        binding.imageBackButton4.setOnClickListener {
            //return filter values
            findNavController().popBackStack()
        }
    }
}