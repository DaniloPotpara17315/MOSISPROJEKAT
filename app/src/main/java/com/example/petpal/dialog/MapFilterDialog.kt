package com.example.petpal.dialog

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.util.Pair
import android.util.Pair.create
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.util.Pair.create
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import com.example.petpal.R
import com.example.petpal.databinding.MapFilterDialogBinding
import com.example.petpal.shared_view_models.MainSharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class MapFilterDialog : BottomSheetDialogFragment() {

    private val sharedViewModel:MainSharedViewModel by activityViewModels()
    private lateinit var binding:MapFilterDialogBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

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

        val checkboxFriends = binding.checkBoxFriends
        checkboxFriends.setOnClickListener {
            sharedViewModel.friendsEnabled.value = checkboxFriends.isChecked
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

        sharedViewModel.friendsEnabled.observe(viewLifecycleOwner) {
            checkboxFriends.isChecked = it
            setFragmentResult("friendMarkers",
                bundleOf("friendMarkers" to it)
                )
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

        binding.buttonFilterName.setOnClickListener {
            dismiss()
            setFragmentResult("eventsFilteredByName",
                bundleOf("eventsFilteredByName" to binding.edittextFilterName.text.toString()))
        }

        binding.buttonFilterDate.setOnClickListener {
            val datepick = MaterialDatePicker
                .Builder
                .dateRangePicker()
                .setTitleText("Select dates")
                .setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(),MaterialDatePicker.todayInUtcMilliseconds()))
                .setTheme(R.style.ThemeOverlay_App_DatePicker)
                .build()

            fragmentManager?.let { it1 -> datepick.show(it1,"Picker") }
            datepick.addOnPositiveButtonClickListener {

                val simpleFormat = SimpleDateFormat("dd/MM/yyyy")
                val formattedDateBegin = simpleFormat.format(it.first)
                val formattedDateEnd = simpleFormat.format(it.second)

                setFragmentResult("filteredEventsByDate",
                    bundleOf(
                        "dateBegin" to formattedDateBegin,
                        "dateEnd" to formattedDateEnd
                    ))
                setFragmentResult("dismiss", bundleOf())
                dismiss()
            }
        }
        setFragmentResultListener("dismiss") {_,_->
            dismiss()
        }
    }
}