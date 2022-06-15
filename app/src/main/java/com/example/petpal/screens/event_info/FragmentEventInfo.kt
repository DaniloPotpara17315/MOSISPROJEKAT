package com.example.petpal.screens.event_info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.petpal.databinding.FragmentEventInfoBinding
import com.example.petpal.shared_view_models.MainSharedViewModel

class FragmentEventInfo : Fragment() {

    private val sharedView : MainSharedViewModel by activityViewModels()

    private lateinit var binding:FragmentEventInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewEventTitle.text = sharedView.selectedEvent!!.name

        binding.imageBackButton.setOnClickListener {
            //back button
        }

        binding.imageEventList.setOnClickListener{
            //attendees list
        }

        binding.imageEventConfirm.setOnClickListener{
            //confirm arrival
        }

    }

}