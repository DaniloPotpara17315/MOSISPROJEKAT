package com.example.petpal.dialog

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.petpal.R
import com.example.petpal.databinding.EventAddDialogBinding
import com.example.petpal.models.Event
import com.example.petpal.shared_view_models.MainSharedViewModel
import com.example.petpal.shared_view_models.MapAddEventViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.FirebaseDatabase
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
            dismiss()
        }

        binding.buttonCreate.setOnClickListener {
            //create event logic
            mapAddEventViewModel.naziv.value = binding.editTextEventName.text.toString()
            mapAddEventViewModel.opis.value = binding.editTextEventDescription.text.toString()

            addEvent()
        }
        binding.buttonCancel.setOnClickListener {
            //cancel event logic
            mapAddEventViewModel.clearData()
            dismiss()
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

    private fun addEvent() {
        val database =
            FirebaseDatabase.getInstance("https://paw-pal-7f105-default-rtdb.europe-west1.firebasedatabase.app/")
        val dataRef =
            database.reference// << Ovde podesavas sta gadjas iz realtime-a U ovo slucaju je test

        val newEvent = mapOf(
            "eventName" to mapAddEventViewModel.naziv.value,
            "eventDesc" to mapAddEventViewModel.opis.value,
            "eventDate" to mapAddEventViewModel.datum.value,
            "eventLon" to mapAddEventViewModel.longitude.value,
            "eventLat" to mapAddEventViewModel.latitude.value,

            )

        dataRef.child("map").child("events").push().setValue(newEvent).addOnCompleteListener{
            if(it.isSuccessful)
            {
                setFragmentResult("noviEvent", bundleOf("noviEvent" to true))
                Toast.makeText(requireContext(), "Uspesno napravljen dogadjaj!", Toast.LENGTH_SHORT).show()
                mapAddEventViewModel.clearData()
                dismiss()
            }
            else{
                Log.d("Task","Error ${it.exception}")
                dismiss()
            }
        }
    }
}