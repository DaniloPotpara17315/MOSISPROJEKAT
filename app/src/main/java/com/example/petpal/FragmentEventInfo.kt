package com.example.petpal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.petpal.databinding.FragmentEventInfoBinding

class FragmentEventInfo : Fragment() {

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