package com.example.petpal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.petpal.databinding.FragmentEventNotifBinding

class FragmentEventNotif : DialogFragment() {


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
        }
        binding.imageView5.setOnClickListener{
            //location event
        }
    }
}