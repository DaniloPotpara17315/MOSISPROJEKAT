package com.example.petpal

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.petpal.databinding.FragmentEventNotifBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class FragmentEventNotif : BottomSheetDialogFragment() {


    private lateinit var binding:FragmentEventNotifBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEventNotifBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonCreate.setOnClickListener {
            //create event logic
        }
        binding.buttonCancel.setOnClickListener {
            //cancel event logic
        }
        binding.imageView3.setOnClickListener{
            //calendar event
            var selectedDate=""
            val today = Calendar.getInstance()
            val year = today.get(Calendar.YEAR)
            val month = today.get(Calendar.MONTH)
            val day= today.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(requireContext(),
                DatePickerDialog.OnDateSetListener{ view, yr, mnth, dayOfMonth->
                selectedDate="${dayOfMonth}/${mnth+1}/${yr}"
                binding.textEventDate.text=selectedDate.toString()
            },year,month,day)
            dpd.show()
        }
        binding.imageView5.setOnClickListener{
            //location event
        }
    }
}