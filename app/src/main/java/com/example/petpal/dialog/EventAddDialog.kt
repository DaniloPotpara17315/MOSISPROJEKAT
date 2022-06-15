package com.example.petpal.dialog

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.petpal.R
import com.example.petpal.databinding.EventAddDialogBinding
import com.example.petpal.shared_view_models.MainSharedViewModel
import com.example.petpal.shared_view_models.MapAddEventViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class EventAddDialog : BottomSheetDialogFragment() {

    private val sharedViewModel : MainSharedViewModel by activityViewModels()
    private val mapAddEventViewModel : MapAddEventViewModel by activityViewModels()
    private lateinit var binding:EventAddDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = EventAddDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapAddEventViewModel.creatingEvent = true
        setOnClickListeners()
        setObservers()
    }

    private fun setOnClickListeners() {
        requireActivity().onBackPressedDispatcher.addCallback {
            mapAddEventViewModel.clearData()
            findNavController().popBackStack()
        }

        binding.buttonCreate.setOnClickListener {
            //create event logic
            mapAddEventViewModel.naziv.value = binding.editTextEventName.text.toString()
            mapAddEventViewModel.opis.value = binding.editTextEventDescription.text.toString()

            mapAddEventViewModel.addEvent()
            dismiss()
        }
        binding.buttonCancel.setOnClickListener {
            //cancel event logic
            mapAddEventViewModel.clearData()

            findNavController().popBackStack()
        }
        binding.buttonAddEventCalendar.setOnClickListener{
            //calendar event
            var selectedDate=""
            val today = Calendar.getInstance()
            val year = today.get(Calendar.YEAR)
            val month = today.get(Calendar.MONTH)
            val day= today.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(requireContext(),
                DatePickerDialog.OnDateSetListener{ _, yr, mnth, dayOfMonth->
                    selectedDate="${dayOfMonth}/${mnth+1}/${yr}"
                    binding.textEventDate.text=selectedDate
                    mapAddEventViewModel.datum.value = selectedDate
                },year,month,day)
            dpd.show()
        }
        binding.buttonAddEventSetCoordinates.setOnClickListener{
            //location event
            mapAddEventViewModel.placingCoordinates = true
            mapAddEventViewModel.naziv.value = binding.editTextEventName.text.toString()
            mapAddEventViewModel.opis.value = binding.editTextEventDescription.text.toString()

            findNavController().navigate(R.id.action_addevent_to_map)

        }
    }
    private fun setObservers() {
        mapAddEventViewModel.naziv.observe(viewLifecycleOwner) {
            binding.editTextEventName.setText(it)
        }

        mapAddEventViewModel.opis.observe(viewLifecycleOwner) {
            binding.editTextEventDescription.setText(it)
        }

        mapAddEventViewModel.datum.observe(viewLifecycleOwner) {
            binding.textEventDate.text = it
        }

        mapAddEventViewModel.longitude.observe(viewLifecycleOwner) {
            if (it!=null)
            binding.textEventCoordinates.text = "" +
                    "${mapAddEventViewModel.longitude.value} +" +
                    "${mapAddEventViewModel.latitude.value}"
        }

        mapAddEventViewModel.latitude.observe(viewLifecycleOwner) {
            if (it!=null)
            binding.textEventCoordinates.text = "" +
                    "${mapAddEventViewModel.longitude.value}, " +
                    "${mapAddEventViewModel.latitude.value}"
        }

    }
}